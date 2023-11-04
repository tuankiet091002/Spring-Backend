package com.java.java_proj.dto.request.forupdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class URequestTrainingProgram {

    @NotNull(message = "Program code is required")
    private Integer code;

    @NotBlank(message = "Program name is required.")
    private String name;

    @NotBlank(message = "General information is required. ")
    private String generalInformation;

    @NotEmpty(message = "Syllabus is required.")
    private List<String> syllabuses;
}
