package com.java.java_proj.services.templates;

import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.entities.TrainingMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface TrainingMaterialService {

    DResponseTrainingMaterial findById(Integer id);

    TrainingMaterial addFile(MultipartFile file);

    void deleteMaterial(Integer id);
}
