package com.java.java_proj.dto.request.forcreate;

import com.java.java_proj.entities.enums.TrainingContentDeliveryType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CRequestTrainingContent {

    @NotBlank(message = "Content name is required.")
    private String content;

    @NotNull(message = "Output standard is required.")
    private String learningObjective;

    // for import csv only
    private String trainingUnitCode;

    @NotNull(message = "Delivery type is required")
    private TrainingContentDeliveryType deliveryType;

    @NotNull(message = "Training time is required")
    @Min(value = 0, message = "Training duration must be greater than 0 min.")
    @Max(value = 480, message = "Training duration must be less then 480 mins.")
    private Integer duration;

    private Boolean trainingFormat;


}
