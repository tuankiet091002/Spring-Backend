package com.java.java_proj.repositories.drafts;

import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;
import com.java.java_proj.entities.drafts.SyllabusDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyllabusDraftRepository extends JpaRepository<SyllabusDraft, String> {

    Integer countByCode(String code);

    Integer countByName(String name);

    @Query("SELECT s FROM SyllabusDraft s JOIN FETCH s.topicOutline WHERE s.code = :code")
    DResponseSyllabus findSyllabusByCode(@Param("code") String code);

    Optional<SyllabusDraft> findFirstByName(String name);

}
