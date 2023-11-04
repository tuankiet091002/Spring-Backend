package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.request.forupdate.URequestSyllabus;
import com.java.java_proj.dto.response.fordetail.*;
import com.java.java_proj.dto.response.forlist.LResponseSyllabus;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.Syllabus;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.ProgramStatus;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.LearningObjectiveRepository;
import com.java.java_proj.repositories.SyllabusRepository;
import com.java.java_proj.repositories.TrainingUnitRepository;
import com.java.java_proj.services.templates.SyllabusService;
import com.java.java_proj.services.templates.TrainingUnitService;
import com.java.java_proj.util.CustomUserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SyllabusServiceImpl implements SyllabusService {

    @Autowired
    private SyllabusRepository syllabusRepository;
    @Autowired
    private TrainingUnitService trainingUnitService;
    @Autowired
    private TrainingUnitRepository trainingUnitRepository;
    @Autowired
    LearningObjectiveRepository learningObjectiveRepository;
    @Autowired
    private ModelMapper modelMapper;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    @Override
    public Page<LResponseSyllabus> findAllByCodeAndNameAndCreatedDate(String code, String name, LocalDate createdDate,
                                                                      String orderBy, String orderDirection,
                                                                      Integer page, Integer size) {

        User owner = getOwner();

        // manual order phrase
        String orderPhrase = orderBy + " " + orderDirection;
        Pageable pageable = PageRequest.of(page - 1, size);
        Integer ownerId = owner == null ? 0 : owner.getId();

        // fetch entity page
        Page<Syllabus> syllabusList = syllabusRepository.findByQuery(code, name, createdDate,
                orderPhrase, pageable, ownerId);
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();

        // project to dto
        return syllabusList.map(entity -> {
            // manual fetch learning objective if draft
            if (entity.getPublishStatus() == ProgramStatus.DRAFT)
                entity.setLearningObjectives(learningObjectiveRepository.findBySyllabusDraft(entity.getCode()));

            // project to interface then return
            return pf.createProjection(LResponseSyllabus.class, entity);
        });
    }

    @Override
    public DResponseSyllabus getSyllabusDetail(String code) {
        DResponseSyllabus syllabus = syllabusRepository.findSyllabusByCode(code);
        if (syllabus == null)
            throw new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found.");

        return syllabus;
    }

    @Override
    public DResponseSyllabus getSyllabusDuplicate(String code) {

        Syllabus syllabus = syllabusRepository.duplicateSyllabusByCode(code)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found."));

        // draft version and new code
        syllabus.nextDraft();
        syllabus.setCode(generateCode(syllabus.getName()));

        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();

        return pf.createProjection(DResponseSyllabus.class, syllabus);
    }

    @Override
    public DResponseSyllabusGeneral getSyllabusGeneral(String code) {
        DResponseSyllabusGeneral syllabusGeneral = syllabusRepository.findSyllabusGeneralByCode(code);
        if (syllabusGeneral == null)
            throw new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found.");

        return syllabusGeneral;
    }

    @Override
    public DResponseSyllabusOutline getSyllabusOutline(String topicCode) {
        // get original syllabus
        Syllabus syllabus = syllabusRepository.findById(topicCode)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found."));
        DResponseSyllabusOutline syllabusOutline = new DResponseSyllabusOutline();
        // manual set code
        syllabusOutline.setCode(topicCode);
        // manual set training unit
        List<DResponseTrainingUnit> trainingUnit = trainingUnitRepository.findAllBySyllabus(syllabus);
        syllabusOutline.setTopicOutline(trainingUnit);

        return syllabusOutline;
    }

    @Override
    @Transactional
    public DResponseSyllabusOthers getSyllabusOthers(String topicCode) {
        // get source syllabus
        Syllabus syllabus = syllabusRepository.findById(topicCode)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found."));
        // model mapper-able
        DResponseSyllabusOthers syllabusOthers = modelMapper.map(syllabus, DResponseSyllabusOthers.class);
        // manual set time allocation data
        List<DResponseTrainingUnit> trainingUnit = trainingUnitRepository.findAllBySyllabus(syllabus);
        syllabusOthers.setTimeAllocation(trainingUnit);
        return syllabusOthers;
    }

    @Override
    public String generateCode(String name) {
        String[] nameArray = name.trim().split("[\\s_-]");
        Random random = new Random();
        StringBuilder code;

        // gen code base on first character of its name
        if (nameArray.length >= 4) {
            List<String> codeArray = Arrays.stream(nameArray).map(x -> x.toUpperCase().substring(0, 1))
                    .collect(Collectors.toList());

            //  join the array
            code = new StringBuilder(String.join("", codeArray));
        } else {
            String joinedName = String.join("", nameArray).toUpperCase();
            code = new StringBuilder(joinedName.substring(0, Math.min(4, joinedName.length())));

            // insert random char until it has length of 4
            while (code.length() < 4) {
                code.append((char) (random.nextInt(26) + 'A'));
            }
        }
        // add random last char to avoid duplicate
        code.append((char) ((random.nextInt(26) + 'A')));
        while (syllabusRepository.countByCode(code.toString()) != 0) {
            code.replace(4, 5, String.valueOf(random.nextInt(26) + 'A'));
        }

        return code.toString();
    }

    @Override
    @Transactional
    public DResponseSyllabus add(CRequestSyllabus requestSyllabus) {

        User owner = getOwner();

        Syllabus syllabus = modelMapper.map(requestSyllabus, Syllabus.class);

        // check duplicate name or code
        if (syllabusRepository.countByCodeOrName(requestSyllabus.getCode(), requestSyllabus.getName()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Duplicate syllabus with same code or name found.");
        }

        // check grade constraints
        if (syllabus.getQuiz() + syllabus.getAssignment() + syllabus.getFinalTotal() != 100
                || syllabus.getFinalTheory() + syllabus.getFinalPractice() != 100)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Total of all assessment is not 100%. Please check again.");

        // set required field
        List<LearningObjective> objectiveList = new ArrayList<>();
        requestSyllabus.getLearningObjectives().forEach(code -> {
            LearningObjective learningObjective = learningObjectiveRepository.findById(code)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Objective not found."));
            objectiveList.add(learningObjective);
        });

        syllabus.setLearningObjectives(objectiveList);
        syllabus.setPublishStatus(ProgramStatus.ACTIVE);
        syllabus.setCreatedDate(LocalDate.now());
        syllabus.setCreatedBy(owner);

        //save
        syllabusRepository.save(syllabus);

        trainingUnitService.addList(syllabus.getCode(), requestSyllabus.getTopicOutlineAdd());

        // change to DTO
        return syllabusRepository.findSyllabusByCode(syllabus.getCode());
    }

    @Override
    public DResponseSyllabus update(URequestSyllabus newSyllabus) {

        User owner = getOwner();

        // check duplicate name or code
        if (syllabusRepository.countByCodeOrName(newSyllabus.getCode(), newSyllabus.getName()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Duplicate syllabus with same code or name found.");
        }

        Syllabus oldSyllabus = syllabusRepository.findById(newSyllabus.getCode())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found"));

        // grade check
        if (newSyllabus.getQuiz() + newSyllabus.getAssignment() + newSyllabus.getFinalTotal() != 100
                || newSyllabus.getFinalTheory() + newSyllabus.getFinalPractice() != 100)
            throw new HttpException(HttpStatus.BAD_REQUEST, "Total of all assessment is not 100%. Please check again.");

        Syllabus syllabus = modelMapper.map(newSyllabus, Syllabus.class);
        // set additional fields
        List<LearningObjective> objectiveList = new ArrayList<>();
        newSyllabus.getLearningObjectives().forEach(code -> {
            LearningObjective learningObjective = learningObjectiveRepository.findById(code)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Objective not found."));
            objectiveList.add(learningObjective);
        });

        syllabus.setLearningObjectives(objectiveList);
        syllabus.setCreatedBy(oldSyllabus.getCreatedBy());
        syllabus.setCreatedDate(oldSyllabus.getCreatedDate());
        syllabus.setModifiedDate(LocalDate.now());
        syllabus.setModifiedBy(owner);

        // version update
        int updateVersion = newSyllabus.getNewVersion();        // new version
        int currentVersion = oldSyllabus.fetchMainVersion();         //old version
        // version logic
        if (updateVersion != currentVersion) {
            syllabus.updateMainVer(updateVersion);
        } else {
            syllabus.nextPublish();
        }

        syllabusRepository.save(syllabus);
        // delete unit
        trainingUnitService.deleteList(newSyllabus.getTopicOutlineDelete());
        // update unit
        trainingUnitService.updateList(newSyllabus.getTopicOutlineUpdate());
        // add new unit
        trainingUnitService.addList(syllabus.getCode(), newSyllabus.getTopicOutlineAdd());

        return syllabusRepository.findSyllabusByCode(syllabus.getCode());
    }

    @Override
    public DResponseSyllabus toggle(String code) {

        User owner = getOwner();

        Syllabus syllabus = syllabusRepository.findById(code)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found."));

        if (syllabus.getPublishStatus() == ProgramStatus.ACTIVE)
            syllabus.setPublishStatus(ProgramStatus.INACTIVE);
        else
            syllabus.setPublishStatus(ProgramStatus.ACTIVE);

        syllabus.setModifiedDate(LocalDate.now());
        syllabus.setModifiedBy(owner);

        syllabusRepository.save(syllabus);

        return syllabusRepository.findSyllabusByCode(code);
    }

}
