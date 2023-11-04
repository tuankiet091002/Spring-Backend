package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.request.forupdate.URequestSyllabus;
import com.java.java_proj.dto.response.fordetail.*;
import com.java.java_proj.dto.response.forlist.LResponseSyllabus;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.drafts.SyllabusDraftService;
import com.java.java_proj.services.templates.SyllabusImportService;
import com.java.java_proj.services.templates.SyllabusService;
import com.java.java_proj.services.templates.TrainingUnitService;
import com.java.java_proj.util.DateFormatter;
import io.swagger.annotations.Api;
import lombok.Cleanup;
import org.apache.commons.io.FilenameUtils;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/syllabus")
@Api(tags = "Syllabus")
public class SyllabusController {

    @Autowired
    SyllabusService syllabusService;
    @Autowired
    SyllabusDraftService syllabusDraftService;
    @Autowired
    TrainingUnitService trainingUnitService;
    @Autowired
    SyllabusImportService syllabusImportService;
    @Autowired
    DateFormatter dateFormatter;


    @GetMapping("")
    public ResponseEntity<Page<LResponseSyllabus>> get(
            @RequestParam(value = "code", defaultValue = "") String code,
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "created-date", defaultValue = "") String createdDateStr,
            @RequestParam(value = "order-by", defaultValue = "createdDate") String orderBy,
            @RequestParam(value = "order-direction", defaultValue = "ASC") String orderDirection,
            @RequestParam(value = "page-no", defaultValue = "1") Integer page,
            @RequestParam(value = "page-size", defaultValue = "10") Integer size) {

        LocalDate createdDate = createdDateStr.isEmpty() ? null :
                dateFormatter.formatDate(createdDateStr);

        List<String> allowedDirections = Arrays.asList("ASC", "DESC");
        if (!allowedDirections.contains(orderDirection)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order direction by " + orderDirection + " is illegal!");
        }

        List<String> allowedFields = Arrays.asList("code", "name", "createdDate", "createdBy", "duration");
        if (!allowedFields.contains(orderBy)) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Order by column " + orderBy + " is illegal!");
        }

        Page<LResponseSyllabus> syllabusPage = syllabusService
                .findAllByCodeAndNameAndCreatedDate(code, name, createdDate, orderBy, orderDirection, page, size);

        if (syllabusPage.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Currently no records.");
        }

        return new ResponseEntity<>(syllabusPage, HttpStatus.OK);
    }

    @GetMapping("/{syllabusCode}/duplicate")
    public ResponseEntity<DResponseSyllabus> viewSyllabusDuplicate(@PathVariable String syllabusCode) {

        DResponseSyllabus syllabus = syllabusService.getSyllabusDuplicate(syllabusCode);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @GetMapping("/{syllabusCode}/general")
    public ResponseEntity<DResponseSyllabusGeneral> viewSyllabusGeneral(@PathVariable String syllabusCode) {

        DResponseSyllabusGeneral syllabus = syllabusService.getSyllabusGeneral(syllabusCode);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @GetMapping("/{syllabusCode}/outline")
    public ResponseEntity<DResponseSyllabusOutline> viewSyllabusOutline(@PathVariable String syllabusCode) {

        DResponseSyllabusOutline syllabus = syllabusService.getSyllabusOutline(syllabusCode);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @GetMapping("/{syllabusCode}/others")
    public ResponseEntity<DResponseSyllabusOthers> viewSyllabusOthers(@PathVariable String syllabusCode) {

        DResponseSyllabusOthers syllabus = syllabusService.getSyllabusOthers(syllabusCode);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @PostMapping("/genCode")
    public ResponseEntity<String> generateCode(@RequestParam(value = "name") String name) {
        String generateCode = syllabusService.generateCode(name);

        return new ResponseEntity<>(generateCode, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<DResponseSyllabus> addSyllabus(@Valid @RequestBody CRequestSyllabus requestSyllabus,
                                                         BindingResult bindingResult) {
        // validator check
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseSyllabus syllabus = syllabusService.add(requestSyllabus);
        syllabus = syllabusService.getSyllabusDetail(syllabus.getCode());

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }


    @PostMapping("/draft")
    public ResponseEntity<DResponseSyllabus> addSyllabusAsDraft(@RequestBody CRequestSyllabus requestSyllabus) {
        // validator check
        if (requestSyllabus.getCode() == null) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Syllabus code is required.");
        }

        DResponseSyllabus syllabus = syllabusDraftService.add(requestSyllabus);
        syllabus = syllabusDraftService.getSyllabusDetail(syllabus.getCode());

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @GetMapping("/draft/{syllabusCode}")
    public ResponseEntity<DResponseSyllabus> getOneSyllabusDraft(@PathVariable String syllabusCode) {

        DResponseSyllabus syllabus = syllabusDraftService.getSyllabusDetail(syllabusCode);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @PostMapping("/from_draft")
    public ResponseEntity<DResponseSyllabus> saveSyllabusFromDraft(@Valid @RequestBody CRequestSyllabus requestSyllabus,
                                                                   BindingResult bindingResult) {
        // validator check
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }
        // delete in draft side
        syllabusDraftService.delete(requestSyllabus.getCode());

        // normal add in main side
        DResponseSyllabus syllabus = syllabusService.add(requestSyllabus);
        syllabus = syllabusService.getSyllabusDetail(syllabus.getCode());

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<DResponseSyllabus> updateSyllabus(@Valid @RequestBody URequestSyllabus requestSyllabus,
                                                            BindingResult bindingResult) {
        // validator check
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseSyllabus syllabus = syllabusService.update(requestSyllabus);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @PutMapping("/{syllabusCode}/toggle")
    public ResponseEntity<DResponseSyllabus> toggleSyllabus(@PathVariable String syllabusCode) {

        DResponseSyllabus syllabus = syllabusService.toggle(syllabusCode);

        return new ResponseEntity<>(syllabus, HttpStatus.OK);
    }

    @PostMapping("/import")
    public ResponseEntity<DResponseSyllabus> importSyllabus(@RequestParam("file") MultipartFile file,
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

        // check file size and extension
        if (file.getSize() > 25000000 || !Objects.equals(FilenameUtils.getExtension(file.getOriginalFilename()), "xlsx")) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "File is invalid. Please check and upload again.");
        }


        try {
            @Cleanup
            InputStream inputStream = file.getInputStream();
            DResponseSyllabus syllabus = syllabusImportService.importSyllabus(inputStream, scanningOption, handleOption);

            syllabus = syllabusDraftService.getSyllabusDetail(syllabus.getCode());

            return new ResponseEntity<>(syllabus, HttpStatus.OK);
        } catch (IOException e) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "Fail to import syllabus from imported file");
        }
    }


    @PostMapping("/unit/{unitCode}/upload")
    public ResponseEntity<DResponseTrainingMaterial> uploadMaterialToUnit(@PathVariable String unitCode,
                                                                          @RequestParam(value = "file") MultipartFile file) {

        DResponseTrainingMaterial trainingMaterial = trainingUnitService.addMaterial(unitCode, file);

        return new ResponseEntity<>(trainingMaterial, HttpStatus.OK);
    }

}
