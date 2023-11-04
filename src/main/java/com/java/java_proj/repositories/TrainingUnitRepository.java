package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseTrainingUnit;
import com.java.java_proj.entities.Syllabus;
import com.java.java_proj.entities.TrainingUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingUnitRepository extends JpaRepository<TrainingUnit, String> {

    List<DResponseTrainingUnit> findAllBySyllabus(Syllabus syllabus);

    @Query(value = "SELECT u FROM TrainingUnit u JOIN u.syllabus s WHERE s.code = :syllabusCode")
    List<TrainingUnit> findAllTU(@Param("syllabusCode") String syllabusCode);

    @Query(value = "SELECT t.unit_code FROM training_units t WHERE t.topic_code=?1", nativeQuery = true)
    List<String> getCodeListBySyllabus(String code);

    List<TrainingUnit> findTrainingUnitBySyllabusCode(String code);

    Integer countTrainingUnitByName(String name);
}
