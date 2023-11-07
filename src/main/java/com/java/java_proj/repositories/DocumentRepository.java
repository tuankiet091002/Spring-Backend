package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.fordetail.LResponseDocument;
import com.java.java_proj.entities.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Page<LResponseDocument> findAllBy(Pageable pageable);

    Optional<DResponseDocument> findFirstById(Integer id);

    Integer countById(Integer id);
}
