package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.entities.drafts.TrainingProgramDraft;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.SyllabusRepository;
import com.java.java_proj.repositories.drafts.TrainingProgramDraftRepository;
import com.java.java_proj.services.drafts.TrainingProgramDraftService;
import com.java.java_proj.services.templates.TrainingProgramImportService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class TrainingProgramImportServiceImpl implements TrainingProgramImportService {

    @Autowired
    TrainingProgramDraftService trainingProgramDraftService;
    @Autowired
    TrainingProgramDraftRepository trainingProgramDraftRepository;
    @Autowired
    SyllabusRepository syllabusRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    Validator validator;

    @Override
    public List<DResponseTrainingProgram> importProgram(InputStream inputStream, String scanningOption, String handleOption) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            List<DResponseTrainingProgram> trainingPrograms = new ArrayList<>();

            XSSFSheet sheet = workbook.getSheetAt(0);

            int rowIndex = 0;

            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }

                Iterator<Cell> cellIterable = row.iterator();

                // fill the DTO
                CRequestTrainingProgram trainingProgram = new CRequestTrainingProgram();
                while (cellIterable.hasNext()) {
                    Cell cell = cellIterable.next();

                    int cellIndex = cell.getColumnIndex();
                    switch (cellIndex) {
                        case 0:
                            trainingProgram.setName(cell.getStringCellValue());
                            break;
                        case 1:
                            trainingProgram.setGeneralInformation(cell.getStringCellValue());
                            break;
                        case 2:
                            trainingProgram.setSyllabuses(Arrays.asList(cell.getStringCellValue().split(",")));
                            break;
                    }
                }

                // check duplicate
                TrainingProgramDraft duplicateProgram = findDuplicate(trainingProgram, scanningOption);
                if (duplicateProgram != null) {
                    switch (handleOption) {
                        case "allow":
                            handleAllow(trainingProgram, scanningOption);
                            break;
                        case "replace":
                            handleReplace(duplicateProgram);
                            break;
                        case "skip":
                            return null;
                    }
                }
                // validate the DTO
                DataBinder binder = new DataBinder(trainingProgram);
                binder.setValidator(validator);
                binder.validate();
                BindingResult bindingResult = binder.getBindingResult();
                if (bindingResult.hasErrors()) {
                    throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
                }

                trainingPrograms.add(trainingProgramDraftService.add(trainingProgram));
            }

            return trainingPrograms;
        } catch (IOException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Failed to import program.");
        }

    }

    @Override
    public void handleAllow(CRequestTrainingProgram request, String scanningOption) {

        throw new HttpException(HttpStatus.BAD_REQUEST, "Duplicated program found, please change the input's content.");
    }

    @Override
    public void handleReplace(TrainingProgramDraft duplicate) {

        trainingProgramDraftService.delete(duplicate.getCode());
    }

    @Override
    public TrainingProgramDraft findDuplicate(CRequestTrainingProgram requestProgram, String scanningOption) {

        if (scanningOption.equals("name")) {
            return trainingProgramDraftRepository.findFirstByName(requestProgram.getName()).orElse(null);
        }
        return null;
    }
}
