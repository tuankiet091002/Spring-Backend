package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestLearningObjective;
import com.java.java_proj.dto.request.forupdate.URequestLearningObjective;
import com.java.java_proj.dto.response.fordetail.DResponseLearningObjective;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.LearningObjectiveService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/objective")
@Api(tags = "Learning Objective")
public class LearningObjectiveController {

    @Autowired
    private LearningObjectiveService learningObjectiveService;

    @GetMapping("")
    private ResponseEntity<List<DResponseLearningObjective>> get(@RequestParam(value = "code", defaultValue = "") String code,
                                                                 @RequestParam(value = "name", defaultValue = "") String name) {

        List<DResponseLearningObjective> objectiveList = learningObjectiveService.getByCodeAndName(code, name);

        return new ResponseEntity<>(objectiveList, HttpStatus.OK);
    }

    @PostMapping("")
    private ResponseEntity<DResponseLearningObjective> add(@Valid @RequestBody CRequestLearningObjective requestObjective,
                                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseLearningObjective learningObjective = learningObjectiveService.add(requestObjective);

        return new ResponseEntity<>(learningObjective, HttpStatus.OK);
    }

    @PutMapping("")
    private ResponseEntity<DResponseLearningObjective> update(@Valid @RequestBody URequestLearningObjective requestObjective,
                                                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseLearningObjective learningObjective = learningObjectiveService.update(requestObjective);

        return new ResponseEntity<>(learningObjective, HttpStatus.OK);
    }
}
