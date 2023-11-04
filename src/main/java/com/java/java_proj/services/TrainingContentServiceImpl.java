package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import com.java.java_proj.dto.request.forupdate.URequestTrainingContent;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.TrainingContent;
import com.java.java_proj.entities.TrainingUnit;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.LearningObjectiveRepository;
import com.java.java_proj.repositories.TrainingContentRepository;
import com.java.java_proj.repositories.TrainingUnitRepository;
import com.java.java_proj.services.templates.TrainingContentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingContentServiceImpl implements TrainingContentService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    TrainingContentRepository trainingContentRepository;
    @Autowired
    LearningObjectiveRepository learningObjectiveRepository;
    @Autowired
    TrainingUnitRepository trainingUnitRepository;

    @Override
    public void addList(String dummyCode, List<CRequestTrainingContent> trainingContentList) {

        trainingContentList.forEach(content -> {
            TrainingContent newContent = modelMapper.map(content, TrainingContent.class);

            LearningObjective learningObjective = learningObjectiveRepository.findById(content.getLearningObjective())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Learning objective not found."));
            newContent.setLearningObjective(learningObjective);

            // set training unit for content
            TrainingUnit unit = new TrainingUnit();
            unit.setCode(dummyCode);
            newContent.setTrainingUnit(unit);

            trainingContentRepository.save(newContent);
        });

    }

    @Override
    public void modifyList(List<URequestTrainingContent> trainingContentList) {

        trainingContentList.forEach(content -> {

            TrainingContent updateContent = trainingContentRepository.findById(content.getId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Update content not found"));
            updateContent.setContent(content.getContent());
            updateContent.setDeliveryType(content.getDeliveryType());
            updateContent.setDuration(content.getDuration());
            updateContent.setTrainingFormat(content.getTrainingFormat());
//            updateContent.setTrainingMaterial(content.getTrainingMaterial());

            // set learning objective
            LearningObjective objective = learningObjectiveRepository.findById(content.getLearningObjective())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Objective not found"));
            updateContent.setLearningObjective(objective);

            trainingContentRepository.save(updateContent);
        });
    }

    @Override
    public void deleteList(List<Integer> trainingContentList) {

        trainingContentList.forEach(content -> {
            TrainingContent deleteContent = trainingContentRepository.findById(content)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Delete content not found"));

            trainingContentRepository.delete(deleteContent);
        });

    }

    @Override
    public void add(CRequestTrainingContent request) {
        try {
            if (trainingContentRepository.countTrainingContentByContent(request.getContent()) > 0) {
                System.out.println("Duplicated training content");
                throw new HttpException(HttpStatus.BAD_REQUEST, "Duplicated training content");
            }
            TrainingContent trainingContent = modelMapper.map(request, TrainingContent.class);

            trainingContentRepository.save(trainingContent);
        } catch (Exception e) {
            System.out.println("Failed to save training content");
            throw new HttpException(HttpStatus.BAD_REQUEST, "Failed to save training content");
        }
    }

}
