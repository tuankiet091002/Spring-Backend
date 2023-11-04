package com.java.java_proj.services;

import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.entities.TrainingMaterial;
import com.java.java_proj.entities.User;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.TrainingMaterialRepository;
import com.java.java_proj.services.templates.TrainingMaterialService;
import com.java.java_proj.util.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class TrainingMaterialServiceImpl implements TrainingMaterialService {

    @Autowired
    FirebaseFileService fileService;
    @Autowired
    TrainingMaterialRepository trainingMaterialRepository;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    @Override
    public DResponseTrainingMaterial findById(Integer id) {
        return trainingMaterialRepository.findOneById(id);
    }

    @Override
    public TrainingMaterial addFile(MultipartFile file) {

        User owner = getOwner();

        try {

            // save to cloud
            String generatedName = fileService.save(file);
            String imageUrl = fileService.getImageUrl(generatedName);

            // create entity
            TrainingMaterial trainingMaterial = new TrainingMaterial();
            trainingMaterial.setName(file.getOriginalFilename());
            trainingMaterial.setGeneratedName(generatedName);
            trainingMaterial.setUrl(imageUrl);
            trainingMaterial.setCreatedDate(LocalDate.now());
            trainingMaterial.setCreatedBy(owner);

            return trainingMaterialRepository.save(trainingMaterial);

        } catch (Exception e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save file");
        }
    }

    @Override
    public void deleteMaterial(Integer id) {

        TrainingMaterial trainingMaterial = trainingMaterialRepository.findById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Training material not found."));

        // delete entity
        trainingMaterialRepository.deleteById(id);
    }
}
