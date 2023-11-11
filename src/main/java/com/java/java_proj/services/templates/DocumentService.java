package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;

import java.util.List;

public interface DocumentService {

    List<DResponseDocument> findAll();

    DResponseDocument findById(Integer id);

    DResponseDocument createDocument(CRequestDocument requestDocument);

    DResponseDocument updateDocument(URequestDocument requestDocument);

    void deleteFile(Integer id);
}
