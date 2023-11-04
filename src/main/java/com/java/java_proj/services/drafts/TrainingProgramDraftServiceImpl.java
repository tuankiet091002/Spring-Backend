package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.request.forupdate.URequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.entities.Syllabus;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.drafts.TrainingProgramDraft;
import com.java.java_proj.entities.enums.ProgramStatus;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.SyllabusRepository;
import com.java.java_proj.repositories.drafts.TrainingProgramDraftRepository;
import com.java.java_proj.util.CustomUserDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainingProgramDraftServiceImpl implements TrainingProgramDraftService {

    @Autowired
    TrainingProgramDraftRepository trainingProgramDraftRepository;
    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    ModelMapper modelMapper;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }


    @Override
    public DResponseTrainingProgram getProgramDetail(Integer code) {
        return trainingProgramDraftRepository.findProgramByCode(code);
    }

    @Override
    public DResponseTrainingProgram add(CRequestTrainingProgram requestProgram) {

        User owner = getOwner();

        TrainingProgramDraft trainingProgramDraft = modelMapper.map(requestProgram, TrainingProgramDraft.class);

        // set required field
        List<Syllabus> syllabusList = new ArrayList<>();
        requestProgram.getSyllabuses().forEach(code -> {
            Syllabus syllabus = syllabusRepository.findById(code)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found."));
            syllabusList.add(syllabus);
        });

        trainingProgramDraft.setSyllabuses(syllabusList);
        trainingProgramDraft.setStatus(ProgramStatus.DRAFT);
        trainingProgramDraft.setCreatedBy(owner);
        trainingProgramDraft.setCreatedDate(LocalDate.now());

        trainingProgramDraft = trainingProgramDraftRepository.save(trainingProgramDraft);

        return trainingProgramDraftRepository.findProgramByCode(trainingProgramDraft.getCode());
    }

    @Override
    public DResponseTrainingProgram update(URequestTrainingProgram requestProgram) {

        TrainingProgramDraft trainingProgramDraft = trainingProgramDraftRepository.findById(requestProgram.getCode())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training program not found."));
        trainingProgramDraft.setName(requestProgram.getName());
        trainingProgramDraft.setGeneralInformation(requestProgram.getGeneralInformation());

        // set required field
        List<Syllabus> syllabusList = new ArrayList<>();
        requestProgram.getSyllabuses().forEach(code -> {
            Syllabus syllabus = syllabusRepository.findById(code)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Syllabus not found."));
            syllabusList.add(syllabus);
        });

        trainingProgramDraft.setSyllabuses(syllabusList);

        trainingProgramDraft = trainingProgramDraftRepository.save(trainingProgramDraft);

        return trainingProgramDraftRepository.findProgramByCode(trainingProgramDraft.getCode());
    }

    @Override
    public void delete(Integer code) {
        TrainingProgramDraft trainingProgramDraft = trainingProgramDraftRepository.findById(code)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Program not found"));

        trainingProgramDraftRepository.deleteById(trainingProgramDraft.getCode());
    }
}
