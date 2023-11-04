package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.request.forupdate.URequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.dto.response.forlist.LResponseTrainingProgram;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.drafts.TrainingProgramDraftService;
import com.java.java_proj.services.templates.TrainingProgramImportService;
import com.java.java_proj.services.templates.TrainingProgramService;
import io.swagger.annotations.Api;
import lombok.Cleanup;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/program")
@Api(tags = "Training Program")
public class TrainingProgramController {

    @Autowired
    TrainingProgramService trainingProgramService;
    @Autowired
    TrainingProgramDraftService trainingProgramDraftService;
    @Autowired
    TrainingProgramImportService trainingProgramImportService;
    @Autowired
    ModelMapper modelMapper;

    @GetMapping("")
    public ResponseEntity<Page<LResponseTrainingProgram>> getAllTrainingProgram(
            @RequestParam(value = "code", defaultValue = "0") Integer code,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "order-by", defaultValue = "createdDate") String orderBy,
            @RequestParam(value = "page-no", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer size,
            @RequestParam(value = "order_direction", defaultValue = "DESC") String orderDirection) {

        // check valid sort criteria
        List<String> allowedFields = Arrays.asList("code", "name", "createdDate", "createdBy", "duration");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        // check valid sort direction
        List<String> allowedSortDirection = Arrays.asList("ASC", "DESC");
        if (!allowedSortDirection.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order direction " + orderDirection + " is illegal!");
        }

        Page<LResponseTrainingProgram> trainingProgramPage = trainingProgramService.
                findAllByCodeAndName(code, name, orderBy, page, size, orderDirection);

        if (trainingProgramPage.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Currently no records.");
        }

        return new ResponseEntity<>(trainingProgramPage, HttpStatus.OK);
    }

    @GetMapping("{programCode}")
    public ResponseEntity<DResponseTrainingProgram> viewDetailTrainingProgram(@PathVariable int programCode) {
        DResponseTrainingProgram program = trainingProgramService.viewDetail(programCode);
        return new ResponseEntity<>(program, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<DResponseTrainingProgram> addTrainingProgram(@Valid @RequestBody CRequestTrainingProgram requestProgram,
                                                                       BindingResult bindingResult) {
        // validator check
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseTrainingProgram trainingProgram = trainingProgramService.add(requestProgram);

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @PostMapping("/draft")
    public ResponseEntity<DResponseTrainingProgram> addTrainingProgramAsDraft(@RequestBody CRequestTrainingProgram requestProgram) {

        DResponseTrainingProgram trainingProgram = trainingProgramDraftService.add(requestProgram);
        trainingProgram = trainingProgramDraftService.getProgramDetail(trainingProgram.getCode());

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @PutMapping("/draft")
    public ResponseEntity<DResponseTrainingProgram> updateTrainingProgramDraft(@RequestBody URequestTrainingProgram requestProgram) {

        DResponseTrainingProgram trainingProgram = trainingProgramDraftService.update(requestProgram);

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @GetMapping("/draft/{programCode}")
    public ResponseEntity<DResponseTrainingProgram> getOneTrainingProgramDraft(@PathVariable Integer programCode) {

        DResponseTrainingProgram trainingProgram = trainingProgramDraftService.getProgramDetail(programCode);

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @PostMapping("/from_draft")
    public ResponseEntity<DResponseTrainingProgram> saveTrainingProgramFromDraft(@Valid @RequestBody URequestTrainingProgram requestTrainingProgram,
                                                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }
        // delete in draft side
        trainingProgramDraftService.delete(requestTrainingProgram.getCode());

        // convert to request dto
        CRequestTrainingProgram cRequestProgram = modelMapper.map(requestTrainingProgram, CRequestTrainingProgram.class);

        // normal add in main side
        DResponseTrainingProgram trainingProgram = trainingProgramService.add(cRequestProgram);
        trainingProgram = trainingProgramService.viewDetail(trainingProgram.getCode());

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<DResponseTrainingProgram> updateTrainingProgram(@Valid @RequestBody URequestTrainingProgram requestTrainingProgram,
                                                                          BindingResult bindingResult) {
        // validator check
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseTrainingProgram trainingProgram = trainingProgramService.update(requestTrainingProgram);

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @PutMapping("/{programCode}/toggle")
    public ResponseEntity<DResponseTrainingProgram> toggleTrainingProgram(@PathVariable Integer programCode) {

        DResponseTrainingProgram trainingProgram = trainingProgramService.toggle(programCode);

        return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
    }

    @PostMapping("/{programCode}/upload")
    public ResponseEntity<DResponseTrainingMaterial> uploadMaterialToProgram(@PathVariable Integer programCode,
                                                                             @RequestParam(value = "file") MultipartFile file) {


        DResponseTrainingMaterial trainingMaterial = trainingProgramService.addMaterial(programCode, file);

        return new ResponseEntity<>(trainingMaterial, HttpStatus.OK);
    }

    @PostMapping("/import")
    public ResponseEntity<List<DResponseTrainingProgram>> importTrainingProgram(@RequestParam("file") MultipartFile file,
                                                                                @RequestParam("scanning-option") String scanningOption,
                                                                                @RequestParam("handle-option") String handleOption) {

        List<String> allowedScans = Arrays.asList("code", "name");
        if (!allowedScans.contains(scanningOption)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Scanning option " + scanningOption + " is illegal!");
        }

        List<String> allowedHandles = Arrays.asList("allow", "replace", "skip");
        if (!allowedHandles.contains(handleOption)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Handle option by column " + handleOption + " is illegal!");
        }

        if (file.getSize() > 25000000 || !Objects.equals(FilenameUtils.getExtension(file.getOriginalFilename()), "xlsx")) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "File is invalid. Please check and upload again.");
        }

        try {
            @Cleanup
            InputStream inputStream = file.getInputStream();
            List<DResponseTrainingProgram> trainingProgram = trainingProgramImportService.importProgram(inputStream, scanningOption, handleOption);

            return new ResponseEntity<>(trainingProgram, HttpStatus.OK);
        } catch (IOException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Fail to import program from imported file");
        }
    }

}
