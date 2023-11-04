package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseLearningObjective;
import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.enums.ObjectiveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningObjectiveRepository extends JpaRepository<LearningObjective, String> {

    List<DResponseLearningObjective> findByCodeContainingAndNameContaining(String code, String name);

    DResponseLearningObjective findByCode(String code);

    List<LearningObjective> findAllByType(ObjectiveType skill);

    @Query(value = "SELECT s.learningObjectives FROM SyllabusDraft s WHERE s.code = :code")
    List<LearningObjective> findBySyllabusDraft(String code);

}
