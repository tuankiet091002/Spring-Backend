package com.java.java_proj.repositories.drafts;

import com.java.java_proj.entities.drafts.TrainingUnitDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingUnitDraftRepository extends JpaRepository<TrainingUnitDraft, String> {

    @Query(value = "SELECT t.unit_code FROM training_units_draft t WHERE t.topic_code=?1", nativeQuery = true)
    List<String> getCodeListBySyllabus(String code);

}
