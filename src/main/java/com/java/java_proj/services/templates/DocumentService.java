package com.java.java_proj.services.templates;

import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.fordetail.LResponseDocument;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    Page<LResponseDocument> findAll();

    DResponseDocument findById(Integer id);

    DResponseDocument addDocument(String name, String description, MultipartFile file);

    void deleteDocument(Integer id);
}
