package com.java.java_proj.dto.request.forupdate;

import com.java.java_proj.entities.enums.ObjectiveType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class URequestLearningObjective {

    @NotBlank(message = "Objective code cannot be empty.")
    private String code;

    @NotBlank(message = "Objective name can not be empty")
    private String name;

    @NotNull(message = "Objective type can not be empty")
    private ObjectiveType type;

    @NotBlank(message = "Objective description can not be empty")
    private String description;

}
