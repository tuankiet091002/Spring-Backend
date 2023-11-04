package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestLearningObjective;
import com.java.java_proj.dto.request.forupdate.URequestLearningObjective;
import com.java.java_proj.dto.response.fordetail.DResponseLearningObjective;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.enums.ObjectiveType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.LearningObjectiveRepository;
import com.java.java_proj.services.templates.LearningObjectiveService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningObjectiveServiceImpl implements LearningObjectiveService {

    @Autowired
    LearningObjectiveRepository learningObjectiveRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<DResponseLearningObjective> getByCodeAndName(String code, String name) {
        return learningObjectiveRepository.findByCodeContainingAndNameContaining(code, name);
    }

    @Override
    public DResponseLearningObjective add(CRequestLearningObjective request) {

        LearningObjective learningObjective = modelMapper.map(request, LearningObjective.class);
        // set the code
        learningObjective.setCode(this.autoGenerateCode(request.getType()));
        learningObjectiveRepository.save(learningObjective);

        return learningObjectiveRepository.findByCode(learningObjective.getCode());
    }

    @Override
    public DResponseLearningObjective update(URequestLearningObjective requestObjective) {

        // check existence
        LearningObjective learningObjective = learningObjectiveRepository.findById(requestObjective.getCode())
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Learning objective not found"));

        // set and save
        learningObjective.setName(requestObjective.getName());
        learningObjective.setDescription(requestObjective.getDescription());
        learningObjectiveRepository.save(learningObjective);

        return learningObjectiveRepository.findByCode(learningObjective.getCode());
    }


    public String autoGenerateCode(ObjectiveType type) {
        // find next index
        int nextIndex = 1;
        List<LearningObjective> learningObjectiveList = learningObjectiveRepository.findAllByType(type);
        nextIndex = learningObjectiveList.size() + 1;

        return type.getValue().toUpperCase().charAt(0) + String.format("%02d", nextIndex);
    }

}
