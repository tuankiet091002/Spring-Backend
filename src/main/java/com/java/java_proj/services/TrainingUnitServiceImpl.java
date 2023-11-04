package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingUnit;
import com.java.java_proj.dto.request.forupdate.URequestTrainingUnit;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.entities.Syllabus;
import com.java.java_proj.entities.TrainingContent;
import com.java.java_proj.entities.TrainingMaterial;
import com.java.java_proj.entities.TrainingUnit;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.TrainingUnitRepository;
import com.java.java_proj.services.templates.TrainingContentService;
import com.java.java_proj.services.templates.TrainingMaterialService;
import com.java.java_proj.services.templates.TrainingUnitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class TrainingUnitServiceImpl implements TrainingUnitService {
    @Autowired
    TrainingUnitRepository trainingUnitRepository;
    @Autowired
    TrainingContentService trainingContentService;
    @Autowired
    TrainingMaterialService trainingMaterialService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void addList(String dummyCode, List<CRequestTrainingUnit> newList) {
        // get new count
        AtomicReference<Integer> nextCount = new AtomicReference<>(trainingUnitRepository.getCodeListBySyllabus(dummyCode)
                .stream()
                .map(code -> code.replace(dummyCode + "_", ""))
                .mapToInt(Integer::parseInt).max().orElse(0) + 1);

        // loop and set
        newList.forEach(unit -> {
            // generate unit code
            String unitCode = dummyCode + "_" + String.format("%02d", nextCount.getAndSet(nextCount.get() + 1));

            TrainingUnit newUnit = modelMapper.map(unit, TrainingUnit.class);
            newUnit.setCode(unitCode);

            // set Syllabus
            Syllabus dummy = new Syllabus();
            dummy.setCode(dummyCode);
            newUnit.setSyllabus(dummy);

            trainingUnitRepository.save(newUnit);

            trainingContentService.addList(unitCode, unit.getTrainingContentsAdd());
        });

    }

    @Override
    public void updateList(List<URequestTrainingUnit> updateList) {

        // loop and set
        updateList.forEach(unit -> {
            TrainingUnit updateUnit = trainingUnitRepository.findById(unit.getCode())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Update unit not found."));
            // set property
            updateUnit.setName(unit.getName());
            updateUnit.setDayNumber(unit.getDayNumber());

            trainingUnitRepository.save(updateUnit);

            // delete content
            trainingContentService.deleteList(unit.getTrainingContentsDelete());

            // update content
            trainingContentService.modifyList(unit.getTrainingContentsUpdate());

            // add new content
            trainingContentService.addList(unit.getCode(), unit.getTrainingContentsAdd());
        });

    }

    @Override
    public void deleteList(List<String> deleteList) {
        // loop and del
        deleteList.forEach(unit -> {
            TrainingUnit deleteUnit = trainingUnitRepository.findById(unit)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Delete unit not found."));

            trainingUnitRepository.delete(deleteUnit);

            trainingContentService.deleteList(
                    deleteUnit.getTrainingContents()
                            .stream().map(TrainingContent::getId)
                            .collect(Collectors.toList())
            );
        });
    }

    @Override
    @Transactional
    public DResponseTrainingMaterial addMaterial(String unitCode, MultipartFile file) {
        TrainingUnit trainingUnit = trainingUnitRepository.findById(unitCode)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training unit not found"));
        // upload file
        TrainingMaterial trainingMaterial = trainingMaterialService.addFile(file);

        // set and save
        trainingUnit.getTrainingMaterials().add(trainingMaterial);
        trainingUnitRepository.save(trainingUnit);

        return trainingMaterialService.findById(trainingMaterial.getId());
    }

}
