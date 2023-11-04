package com.java.java_proj.repositories;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingContent;
import com.java.java_proj.entities.TrainingContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingContentRepository extends JpaRepository<TrainingContent, Integer> {
    Integer countById(Integer id);

    Integer countTrainingContentByContent(String content);
}
