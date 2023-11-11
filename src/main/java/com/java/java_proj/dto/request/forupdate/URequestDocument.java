package com.java.java_proj.dto.request.forupdate;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class URequestDocument {

    @NotBlank(message = "Document id is required.")
    private Integer id;

    @NotBlank(message = "Document  is required.")
    private String name;

    @NotBlank(message = "Email address is required.")
    private String description;

}
