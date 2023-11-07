package com.java.java_proj.controllers;

import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.dto.response.fordetail.LResponseDocument;
import com.java.java_proj.services.templates.DocumentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
@CrossOrigin(origins = "http://localhost:3000")
@Api(tags = "Document Controller")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Page<LResponseDocument>> getAllDocument() {

        Page<LResponseDocument> documentList = documentService.findAll();

        return new ResponseEntity<>(documentList, HttpStatus.OK);
    }

    @GetMapping("/{docId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<DResponseDocument> getOneDocument(@PathVariable Integer docId) {

        DResponseDocument document = documentService.findById(docId);

        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<DResponseDocument> addDocument(@RequestParam(name = "file") MultipartFile file,
                                                         @RequestParam(name = "name") String name,
                                                         @RequestParam(name = "description") String description) {


        DResponseDocument document = documentService.addDocument(name, description, file);

        return new ResponseEntity<>(document, HttpStatus.OK);
    }


    @DeleteMapping("/{docId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDocument(@PathVariable Integer docId) {
        documentService.deleteDocument(docId);
    }
}

