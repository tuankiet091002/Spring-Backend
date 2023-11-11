package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<DResponseDocument> findOneById(Integer id);

    List<DResponseDocument> findAllBy();

}
