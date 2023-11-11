package com.java.java_proj.controllers;

import com.java.java_proj.dto.request.forcreate.CRequestDocument;
import com.java.java_proj.dto.request.forupdate.URequestDocument;
import com.java.java_proj.dto.response.fordetail.DResponseDocument;
import com.java.java_proj.exceptions.HttpException;
import com.java.java_proj.services.templates.DocumentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;

@RestController
@RequestMapping("/document")
@Api(tags = "Channel")
public class DocumentController {

    @Autowired
    DocumentService documentService;

    @GetMapping()
    public ResponseEntity<List<DResponseDocument>> getAllDocument() {

        List<DResponseDocument> documents = documentService.findAll();

        return new ResponseEntity<>(documents, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DResponseDocument> getOneChannel(@PathVariable Integer documentId) {

        DResponseDocument documents = documentService.findById(documentId);

        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<DResponseDocument> createDocument(@Valid @RequestBody CRequestDocument requestChannel,
                                                          BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseDocument document = documentService.createDocument(requestChannel);

        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<DResponseDocument> updateDocument(@Valid @RequestBody URequestDocument requestChannel,
                                                          BindingResult bindingResult) {

        // get validation error
        if (bindingResult.hasErrors()) {
            throw new HttpException(HttpStatus.BAD_REQUEST, bindingResult);
        }

        DResponseDocument document = documentService.updateDocument(requestChannel);

        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Null> deleteChannel(@PathVariable Integer documentId) {

        documentService.deleteFile(documentId);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
