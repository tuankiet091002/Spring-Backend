package com.java.java_proj.dto.request.forcreate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CRequestTrainingProgram {

    @NotBlank(message = "Program name is required.")
    private String name;

    @NotBlank(message = "General information is required. ")
    private String generalInformation;

    @NotEmpty(message = "Syllabus is required.")
    private List<String> syllabuses;
}
