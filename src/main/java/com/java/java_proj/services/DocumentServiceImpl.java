package com.java.java_proj.services;

import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.fordetail.LResponseDocument;
import com.java.java_proj.entities.Document;
import com.java.java_proj.entities.User;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.repositories.DocumentRepository;
import com.java.java_proj.services.templates.DocumentService;
import com.java.java_proj.util.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentRepository documentRepository;
    @Autowired
    FirebaseFileService fileService;

    private User getOwner() {
        return SecurityContextHolder.getContext().getAuthentication() == null ? null :
                ((CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    @Override
    public Page<LResponseDocument> findAll() {

        Pageable pageable = PageRequest.of(0, 10);
        return documentRepository.findAllBy(pageable);
    }

    @Override
    public DResponseDocument findById(Integer id) {
        return documentRepository.findFirstById(id)
                .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found."));
    }

    @Override
    public DResponseDocument addDocument(String name, String description, MultipartFile file) {
        User owner = getOwner();

        try {

            // save to cloud
            String generatedName = fileService.save(file);
            String imageUrl = fileService.getImageUrl(generatedName);

            // create entity
            Document document = new Document();
            document.setName(name);
            document.setDescription(description);
            document.setFilename(file.getOriginalFilename());
            document.setGeneratedName(generatedName);
            document.setUrl(imageUrl);
            document.setCreatedDate(LocalDate.now());
            document.setCreatedBy(owner);

            document = documentRepository.save(document);

            return documentRepository.findFirstById(document.getId())
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "Document not found."));


        } catch (Exception e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Can't save file");
        }
    }

    @Override
    public void deleteDocument(Integer id) {
        if (documentRepository.countById(id) == 0) {
            throw new HttpException(HttpStatus.NOT_FOUND, "Document not found");
        }

        documentRepository.deleteById(id);
    }
}
