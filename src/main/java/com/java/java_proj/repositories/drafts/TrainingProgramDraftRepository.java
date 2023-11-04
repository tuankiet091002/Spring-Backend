package com.java.java_proj.repositories.drafts;

import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.entities.drafts.TrainingProgramDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingProgramDraftRepository extends JpaRepository<TrainingProgramDraft, Integer> {

    @Query("SELECT d FROM TrainingProgramDraft d JOIN FETCH d.syllabuses WHERE d.code = :code")
    DResponseTrainingProgram findProgramByCode(@Param("code") Integer code);

    Optional<TrainingProgramDraft> findFirstByName(String name);

    Integer countByName(String name);

}
