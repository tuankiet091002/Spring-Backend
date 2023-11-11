package com.java.java_proj.dto.request.forcreate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CRequestDocument {

    @NotBlank(message = "Name  is required.")
    private String name;

    @NotBlank(message = "Email address is required.")
    private String description;

    @NotNull(message = "File is required")
    private MultipartFile file;

}