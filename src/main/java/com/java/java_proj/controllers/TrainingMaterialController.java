package com.java.java_proj.controllers;

import com.java.java_proj.services.templates.TrainingMaterialService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/material")
@Api(tags = "Training Material")
public class TrainingMaterialController {

    @Autowired
    TrainingMaterialService trainingMaterialService;

    @DeleteMapping("/{materialId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaterial(@PathVariable Integer materialId) {

        trainingMaterialService.deleteMaterial(materialId);
    }
}
