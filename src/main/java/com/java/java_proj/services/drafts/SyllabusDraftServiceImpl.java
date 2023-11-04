package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.drafts.SyllabusDraft;
import com.java.java_proj.entities.drafts.TrainingUnitDraft;
import com.java.java_proj.entities.enums.ProgramStatus;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.LearningObjectiveRepository;
import com.java.java_proj.repositories.SyllabusRepository;
import com.java.java_proj.repositories.drafts.SyllabusDraftRepository;
import com.java.java_proj.util.CustomUserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyllabusDraftServiceImpl implements SyllabusDraftService {

    @Autowired
    SyllabusDraftRepository syllabusDraftRepository;
    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    TrainingUnitDraftService trainingUnitDraftService;
    @Autowired
    LearningObjectiveRepository learningObjectiveRepository;
    @Autowired
    ModelMapper modelMapper;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    @Override
    @Transactional
    public DResponseSyllabus getSyllabusDetail(String code) {
        return syllabusDraftRepository.findSyllabusByCode(code);
    }

    @Override
    @Transactional
    public DResponseSyllabus add(CRequestSyllabus requestSyllabus) {

        User owner = getOwner();

        // check duplicate name or code in main
        if (syllabusRepository.countByCodeOrName(requestSyllabus.getCode(), requestSyllabus.getName()) > 0) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Duplicate syllabus with same code or name found.");
        }

        if (syllabusDraftRepository.countByCode(requestSyllabus.getCode()) > 0) {
            delete(requestSyllabus.getCode());
        }

        SyllabusDraft syllabus = modelMapper.map(requestSyllabus, SyllabusDraft.class);

        // set required field
        List<LearningObjective> objectiveList = new ArrayList<>();
        requestSyllabus.getLearningObjectives().forEach(code -> {
            LearningObjective learningObjective = learningObjectiveRepository.findById(code)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Objective not found."));
            objectiveList.add(learningObjective);
        });

        syllabus.setLearningObjectives(objectiveList);
        syllabus.setPublishStatus(ProgramStatus.DRAFT);
        syllabus.setCreatedDate(LocalDate.now());
        syllabus.setCreatedBy(owner);

        //save
        syllabusDraftRepository.save(syllabus);

        trainingUnitDraftService.addList(syllabus.getCode(), requestSyllabus.getTopicOutlineAdd());

        // change to DTO
        return syllabusDraftRepository.findSyllabusByCode(syllabus.getCode());
    }


    @Override
    @Transactional
    public void delete(String code) {
        SyllabusDraft syllabus = syllabusDraftRepository.findById(code)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found"));

        trainingUnitDraftService.deleteList(syllabus.getTopicOutline()
                .stream().map(TrainingUnitDraft::getCode).collect(Collectors.toList()));

        syllabusDraftRepository.deleteById(syllabus.getCode());
    }
}
