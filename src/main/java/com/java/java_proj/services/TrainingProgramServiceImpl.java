package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.request.forupdate.URequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.dto.response.forlist.LResponseTrainingProgram;
import com.java.java_proj.entities.Syllabus;
import com.java.java_proj.entities.TrainingMaterial;
import com.java.java_proj.entities.TrainingProgram;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.ProgramStatus;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.SyllabusRepository;
import com.java.java_proj.repositories.TrainingProgramRepository;
import com.java.java_proj.repositories.UserRepository;
import com.java.java_proj.services.templates.TrainingMaterialService;
import com.java.java_proj.services.templates.TrainingProgramService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingProgramServiceImpl implements TrainingProgramService {

    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    TrainingProgramRepository trainingProgramRepository;
    @Autowired
    TrainingMaterialService trainingMaterialService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    @Override
    public Page<LResponseTrainingProgram> findAllByCodeAndName(Integer code, String name, String orderBy, Integer page,
                                                               Integer size, String orderDirection) {

        User owner = getOwner();

        // manual order phrase
        String orderPhrase = orderBy + " " + orderDirection;
        Pageable pageable = PageRequest.of(page - 1, size);
        Integer ownerId = owner == null ? 0 : owner.getId();

        // fetch entity page
        Page<Object[]> programList = trainingProgramRepository.findByQuery(code, name, orderPhrase, pageable, ownerId);
        ProjectionFactory pf = new SpelAwareProxyProjectionFactory();

        // project to dto
        return programList.map(entity -> {
            // set every single field for DTO
                    TrainingProgram programEntity = new TrainingProgram();
                    programEntity.setCode(Integer.parseInt(entity[0].toString()));
                    programEntity.setDuration(Integer.parseInt(entity[2].toString()));
                    programEntity.setGeneralInformation(entity[3].toString());
                    programEntity.setName(entity[5].toString());
                    programEntity.setStatus(ProgramStatus.valueOf(entity[6].toString()));
                    programEntity.setCreatedDate(LocalDate.parse(entity[1].toString()));
                    programEntity.setCreatedBy(userRepository.findById(Integer.parseInt(entity[7].toString())).orElse(null));

                    return pf.createProjection(LResponseTrainingProgram.class, programEntity);
                }
        );
    }

    @Override
    @Transactional
    public DResponseTrainingProgram viewDetail(int code) {

        return trainingProgramRepository.findOneByCode(code);
    }

    @Override
    public DResponseTrainingProgram add(CRequestTrainingProgram requestProgram) {

        User owner = getOwner();

        // create entity and set fields
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setName(requestProgram.getName());
        trainingProgram.setGeneralInformation(requestProgram.getGeneralInformation());
        trainingProgram.setStatus(ProgramStatus.ACTIVE);
        trainingProgram.setCreatedDate(LocalDate.now());
        trainingProgram.setCreatedBy(owner);

        // set syllabus list
        List<Syllabus> syllabusList = new ArrayList<>();
        requestProgram.getSyllabuses().forEach(syllabusCode -> {

            //check existence and add
            Syllabus syllabus = syllabusRepository.findById(syllabusCode).orElseThrow(
                    () -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus with code " + syllabusCode + " is not found. "));

            syllabusList.add(syllabus);
        });

        trainingProgram.setSyllabuses(syllabusList);

        trainingProgramRepository.save(trainingProgram);

        return trainingProgramRepository.findOneByCode(trainingProgram.getCode());
    }

    @Override
    public DResponseTrainingProgram update(URequestTrainingProgram requestProgram) {

        User owner = getOwner();

        TrainingProgram trainingProgram = trainingProgramRepository.findById(requestProgram.getCode())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training Program not found."));

        trainingProgram.setName(requestProgram.getName());
        trainingProgram.setGeneralInformation(requestProgram.getGeneralInformation());
        trainingProgram.setModifiedDate(LocalDate.now());
        trainingProgram.setModifiedBy(owner);

        // set syllabus list
        List<Syllabus> syllabusList = new ArrayList<>();
        requestProgram.getSyllabuses().forEach(syllabusCode -> {
            //check existence and add
            Syllabus syllabus = syllabusRepository.findById(syllabusCode).orElseThrow(
                    () -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus with code " + syllabusCode + " is not found. "));

            syllabusList.add(syllabus);
        });

        trainingProgram.setSyllabuses(syllabusList);

        trainingProgram = trainingProgramRepository.save(trainingProgram);

        return trainingProgramRepository.findOneByCode(trainingProgram.getCode());
    }

    @Override
    public DResponseTrainingProgram toggle(Integer code) {

        User owner = getOwner();

        TrainingProgram trainingProgram = trainingProgramRepository.findById(code)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training program not found."));

        if (trainingProgram.getStatus() == ProgramStatus.ACTIVE)
            trainingProgram.setStatus(ProgramStatus.INACTIVE);
        else
            trainingProgram.setStatus(ProgramStatus.ACTIVE);

        trainingProgram.setModifiedDate(LocalDate.now());
        trainingProgram.setModifiedBy(owner);

        trainingProgramRepository.save(trainingProgram);

        return trainingProgramRepository.findOneByCode(code);
    }

    @Override
    @Transactional
    public DResponseTrainingMaterial addMaterial(Integer programCode, MultipartFile file) {

        TrainingProgram trainingProgram = trainingProgramRepository.findById(programCode)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training program not found"));

        // upload file
        TrainingMaterial trainingMaterial = trainingMaterialService.addFile(file);

        // set and save
        trainingProgram.getTrainingMaterials().add(trainingMaterial);
        trainingProgramRepository.save(trainingProgram);

        return trainingMaterialService.findById(trainingMaterial.getId());
    }

}
