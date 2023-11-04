package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingUnit;
import com.java.java_proj.entities.drafts.SyllabusDraft;
import com.java.java_proj.entities.drafts.TrainingContentDraft;
import com.java.java_proj.entities.drafts.TrainingUnitDraft;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.drafts.TrainingUnitDraftRepository;
import com.java.java_proj.services.templates.TrainingMaterialService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class TrainingUnitDraftServiceImpl implements TrainingUnitDraftService {

    @Autowired
    TrainingUnitDraftRepository trainingUnitDraftRepository;
    @Autowired
    TrainingContentDraftService trainingContentDraftService;
    @Autowired
    TrainingMaterialService trainingMaterialService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void addList(String dummyCode, List<CRequestTrainingUnit> newList) {
        // get new count
        AtomicReference<Integer> nextCount = new AtomicReference<>(trainingUnitDraftRepository.getCodeListBySyllabus(dummyCode)
                .stream()
                .map(code -> code.replace(dummyCode + "_", ""))
                .mapToInt(Integer::parseInt).max().orElse(0) + 1);

        // loop and set
        newList.forEach(unit -> {
            // generate unit code
            String unitCode = dummyCode + "_" + String.format("%02d", nextCount.getAndSet(nextCount.get() + 1));

            TrainingUnitDraft newUnit = modelMapper.map(unit, TrainingUnitDraft.class);
            newUnit.setCode(unitCode);

            // set Syllabus
            SyllabusDraft dummy = new SyllabusDraft();
            dummy.setCode(dummyCode);
            newUnit.setSyllabus(dummy);

            trainingUnitDraftRepository.save(newUnit);

            trainingContentDraftService.addList(unitCode, unit.getTrainingContentsAdd());
        });

    }

    @Override
    public void deleteList(List<String> deleteList) {
        // loop and del
        deleteList.forEach(unit -> {
            TrainingUnitDraft deleteUnit = trainingUnitDraftRepository.findById(unit)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Delete unit not found."));

            trainingContentDraftService.deleteList(
                    deleteUnit.getTrainingContents()
                            .stream().map(TrainingContentDraft::getId)
                            .collect(Collectors.toList())
            );

            trainingUnitDraftRepository.delete(deleteUnit);
        });


    }
}
