package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.entities.TrainingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingMaterialRepository extends JpaRepository<TrainingMaterial, Integer> {

    DResponseTrainingMaterial findOneById(Integer code);
}
