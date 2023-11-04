package com.java.java_proj.dto.request.forcreate;

import com.java.java_proj.entities.enums.ObjectiveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CRequestLearningObjective {

    @NotBlank(message = "Objective name can not be empty.")
    private String name;

    @NotNull(message = "Objective type can not be empty.")
    private ObjectiveType type;

    @NotBlank(message = "Objective description can not be empty.")
    private String description;

}
