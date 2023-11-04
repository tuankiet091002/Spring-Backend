package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.drafts.TrainingContentDraft;
import com.java.java_proj.entities.drafts.TrainingUnitDraft;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.LearningObjectiveRepository;
import com.java.java_proj.repositories.drafts.TrainingContentDraftRepository;
import com.java.java_proj.repositories.drafts.TrainingUnitDraftRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingContentDraftServiceImpl implements TrainingContentDraftService {

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TrainingContentDraftRepository trainingContentDraftRepository;
    @Autowired
    LearningObjectiveRepository learningObjectiveRepository;
    @Autowired
    TrainingUnitDraftRepository trainingUnitRepository;


    @Override
    public void addList(String dummyCode, List<CRequestTrainingContent> trainingContentList) {

        trainingContentList.forEach(content -> {
            TrainingContentDraft newContent = modelMapper.map(content, TrainingContentDraft.class);

            LearningObjective learningObjective = learningObjectiveRepository.findById(content.getLearningObjective())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Learning objective not found."));
            newContent.setLearningObjective(learningObjective);

            // set training unit for content
            TrainingUnitDraft unit = new TrainingUnitDraft();
            unit.setCode(dummyCode);
            newContent.setTrainingUnit(unit);

            trainingContentDraftRepository.save(newContent);
        });

    }

    @Override
    public void deleteList(List<Integer> trainingContentList) {

        trainingContentList.forEach(content -> {
            TrainingContentDraft deleteContent = trainingContentDraftRepository.findById(content)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Delete content not found"));

            trainingContentDraftRepository.delete(deleteContent);
        });

    }
}
