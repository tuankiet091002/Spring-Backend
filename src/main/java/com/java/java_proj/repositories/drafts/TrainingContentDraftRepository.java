package com.java.java_proj.repositories.drafts;

import com.java.java_proj.entities.drafts.TrainingContentDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingContentDraftRepository extends JpaRepository<TrainingContentDraft, Integer> {

}
