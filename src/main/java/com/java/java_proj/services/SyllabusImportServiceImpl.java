package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestLearningObjective;
import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import com.java.java_proj.dto.request.forcreate.CRequestTrainingUnit;
import com.java.java_proj.dto.response.fordetail.DResponseLearningObjective;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.drafts.SyllabusDraft;
import com.java.java_proj.entities.enums.ObjectiveType;
import com.java.java_proj.entities.enums.TrainingContentDeliveryType;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.drafts.SyllabusDraftRepository;
import com.java.java_proj.services.drafts.SyllabusDraftService;
import com.java.java_proj.services.templates.LearningObjectiveService;
import com.java.java_proj.services.templates.SyllabusImportService;
import com.java.java_proj.services.templates.SyllabusService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SyllabusImportServiceImpl implements SyllabusImportService {

    @Autowired
    SyllabusService syllabusService;
    @Autowired
    SyllabusDraftRepository syllabusDraftRepository;
    @Autowired
    SyllabusDraftService syllabusDraftService;
    @Autowired
    LearningObjectiveService learningObjectiveService;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    Validator validator;

    Workbook workbook;
    DataFormatter dataFormatter = new DataFormatter();
    HashMap<String, String> map = new HashMap<>();

    @Override
    @Transactional
    public DResponseSyllabus importSyllabus(InputStream file, String scanningOption, String handleOption) {
        try {

            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(2);

            // check data existence
            if (row == null) {
                throw new HttpException(HttpStatus.BAD_REQUEST, "Syllabus data not found");
            }

            // fill the DTO
            String[] cellValue = setSyllabusDTO(row);
            CRequestSyllabus requestSyllabus = new CRequestSyllabus(
                    cellValue[0],
                    cellValue[1],  // name
                    cellValue[2],  // level
                    Integer.parseInt(cellValue[3]),  // attendee number
                    cellValue[4],  // technical requirement
                    cellValue[5],  // course objective
                    cellValue[6],  // training principle
                    Integer.parseInt(cellValue[8]),  // quiz
                    Integer.parseInt(cellValue[9]),  // assignment
                    Integer.parseInt(cellValue[10]),  // final theory
                    Integer.parseInt(cellValue[11]),  // final practice
                    Integer.parseInt(cellValue[12]),  // final
                    Integer.parseInt(cellValue[13])  // gpa
            );

            // find duplicate and handle
            SyllabusDraft duplicateSyllabus = findDuplicate(requestSyllabus, scanningOption);

            if (duplicateSyllabus != null) {
                switch (handleOption) {
                    case "allow":
                        handleAllow(requestSyllabus, scanningOption);
                        break;
                    case "replace":
                        handleReplace(duplicateSyllabus);
                        break;
                    case "skip":
                        return null;
                }
            }

            // manual add
            requestSyllabus.setLearningObjectives(importLearningObjective());
            requestSyllabus.setTopicOutlineAdd(importTrainingUnit());

            // validate the DTO
            DataBinder binder = new DataBinder(requestSyllabus);
            binder.setValidator(validator);
            binder.validate();
            BindingResult bindingResult = binder.getBindingResult();
            if (bindingResult.hasErrors()) {
                throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
            }

            syllabusDraftService.add(requestSyllabus);

            return syllabusDraftService.getSyllabusDetail(requestSyllabus.getCode());


        } catch (IOException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Failed to import syllabus");
        }
    }

    @Override
    @Transactional
    public List<String> importLearningObjective() {

        Sheet sheet = workbook.getSheetAt(1);
        List<String> responseList = new ArrayList<>();

        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if (row != null) {
                if (row.getRowNum() < 2) {
                    continue;
                }

                // fill the DTO
                String[] cellValue = setLearningObjectiveDTO(row);
                CRequestLearningObjective request = new CRequestLearningObjective(
                        cellValue[1], // name
                        ObjectiveType.valueOf(cellValue[2]), // type
                        cellValue[3] // description
                );

                DResponseLearningObjective response = learningObjectiveService.add(request);
                LearningObjective objective = modelMapper.map(response, LearningObjective.class);

                // real objective code to map
                map.put(cellValue[0], objective.getCode());

                responseList.add(objective.getCode());
            } else break;
        }

        return responseList;
    }

    @Override
    @Transactional
    public List<CRequestTrainingUnit> importTrainingUnit() {
        Sheet sheet = workbook.getSheetAt(2);

        List<CRequestTrainingUnit> trainingUnitList = new ArrayList<>();
        List<CRequestTrainingContent> trainingContentList = importTrainingContent();

        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if (row != null) {
                if (row.getRowNum() == 2 || row.getRowNum() == 6) {

                    // fill the DTO
                    String[] cellValue = setTrainingUnitDTO(row);
                    CRequestTrainingUnit request = new CRequestTrainingUnit(
                            cellValue[1], // name
                            Integer.parseInt(cellValue[2]),
                            trainingContentList
                    );

                    // loop all content to filter those with different temporal code
                    request.setTrainingContentsAdd(request.getTrainingContentsAdd().stream()
                            .filter(content -> content.getTrainingUnitCode().equals(cellValue[0]))
                            .collect(Collectors.toList()));

                    trainingUnitList.add(request);
                }
            } else break;
        }
        return trainingUnitList;
    }

    @Override
    @Transactional
    public List<CRequestTrainingContent> importTrainingContent() {

        Sheet sheet = workbook.getSheetAt(3);
        List<CRequestTrainingContent> requestList = new ArrayList<>();

        for (int i = 0; i <= sheet.getPhysicalNumberOfRows(); i++) {
            Row row = sheet.getRow(i);

            if (row != null) {
                if (row.getRowNum() < 2) {
                    continue;
                }

                // fill the dto
                String[] cellValue = setTrainingContentDTO(row);
                CRequestTrainingContent request = new CRequestTrainingContent(
                        cellValue[1], // content
                        map.get(cellValue[2]), // learning objective
                        cellValue[0], // temporal training unit
                        TrainingContentDeliveryType.valueOf(cellValue[3]), // delivery type
                        Integer.parseInt(cellValue[4]), // duration
                        Boolean.parseBoolean(cellValue[5]) // training format
                );

                requestList.add(request);
            } else break;
        }
        return requestList;
    }

    @Override
    public String[] setSyllabusDTO(Row row) {
        String[] strArr = new String[14];

        for (int i = 0; i < 14; i++) {
            if (i == 7) {
                continue;
            }

            if (row.getCell(i).getCellType() == CellType.NUMERIC) {
                strArr[i] = dataFormatter.formatCellValue(row.getCell(i));
            } else {
                strArr[i] = row.getCell(i).getStringCellValue();
            }
        }

        return strArr;
    }

    @Override
    public String[] setLearningObjectiveDTO(Row row) {
        String[] strArr = new String[4];

        for (int i = 0; i < 4; i++) {
            if (row.getCell(i).getCellType() == CellType.NUMERIC) {
                strArr[i] = dataFormatter.formatCellValue(row.getCell(i));
            } else {
                strArr[i] = row.getCell(i).getStringCellValue();
            }
        }

        return strArr;
    }

    @Override
    public String[] setTrainingUnitDTO(Row row) {
        String[] strArr = new String[3];

        for (int i = 0; i < 3; i++) {
            if (row.getCell(i).getCellType() == CellType.NUMERIC) {
                strArr[i] = dataFormatter.formatCellValue(row.getCell(i));
            } else {
                strArr[i] = row.getCell(i).getStringCellValue();
            }
        }

        return strArr;
    }

    @Override
    public String[] setTrainingContentDTO(Row row) {
        String[] strArr = new String[7];

        for (int i = 0; i < 7; i++) {
            if (row.getCell(i).getCellType() == CellType.NUMERIC) {
                strArr[i] = dataFormatter.formatCellValue(row.getCell(i));
            } else if (row.getCell(i).getCellType() == CellType.STRING) {
                strArr[i] = row.getCell(i).getStringCellValue();
            } else {
                strArr[i] = String.valueOf(row.getCell(i).getBooleanCellValue());
            }
        }

        return strArr;
    }

    public void handleAllow(CRequestSyllabus request, String scanningOption) {
        throw new HttpException(HttpStatus.BAD_REQUEST, "Duplicated syllabus found, please change the input's content.");
    }

    @Transactional
    public void handleReplace(SyllabusDraft syllabusDraft) {

        syllabusDraftService.delete(syllabusDraft.getCode());
    }

    @Override
    @Transactional
    public SyllabusDraft findDuplicate(CRequestSyllabus requestSyllabus, String scanningOption) {

        if (scanningOption.equals("name")) {
            return syllabusDraftRepository.findFirstByName(requestSyllabus.getName()).orElse(null);
        } else
            return syllabusDraftRepository.findById(requestSyllabus.getCode()).orElse(null);
    }
}
