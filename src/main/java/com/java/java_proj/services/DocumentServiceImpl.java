package com.java.java_proj.services;

import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.entities.Document;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.DocumentRepository;
import com.java.java_proj.services.templates.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    FirebaseFileService fileService;
    @Autowired
    DocumentRepository documentRepository;

    @Override
    public List<DResponseDocument> findAll() {
        return documentRepository.findAllBy();

    }

    @Override
    public DResponseDocument findById(Integer id) {
        return documentRepository.findOneById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Resource not found"));
    }

    @Override
    public DResponseDocument createDocument(CRequestDocument requestDocument) {

        try {

            // save to cloud
            String generatedName = fileService.save(requestDocument.getFile());
            String imageUrl = fileService.getImageUrl(generatedName);

            // create entity
            Document resource = Document.builder()
                    .name(requestDocument.getName())
                    .description(requestDocument.getDescription())
                    .filename(requestDocument.getFile().getOriginalFilename())
                    .generatedName(generatedName)
                    .url(imageUrl)
                    .build();


            resource = documentRepository.save(resource);

            return documentRepository.findOneById(resource.getId()).orElse(null);

        } catch (Exception e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save file");
        }
    }

    @Override
    public DResponseDocument updateDocument(URequestDocument requestDocument) {

            Document document = documentRepository.findById(requestDocument.getId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found."));

            document.setName(requestDocument.getName());
            document.setDescription(requestDocument.getDescription());

            documentRepository.save(document);

            return documentRepository.findOneById(document.getId()).orElse(null);

    }

    @Override
    public void deleteFile(Integer id) {
        try {
            Document resource = documentRepository.findById(id)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found."));

            // delete file on firebase
            fileService.delete(resource.getGeneratedName());

            // delete entity
            documentRepository.deleteById(id);
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't delete file.");
        }
    }
}