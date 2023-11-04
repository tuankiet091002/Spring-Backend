package com.java.java_proj.dto.request.forcreate;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CRequestTrainingUnit {

    @NotBlank(message = "Unit name is required.")
    private String name;

    @NotNull(message = "Day number is required .")
    @Min(value = 1, message = "Day number must be equal or greater than 1")
    private Integer dayNumber;

    @NotEmpty(message = "Please input at least one content into this unit.")
    private List<@Valid CRequestTrainingContent> trainingContentsAdd;
}
