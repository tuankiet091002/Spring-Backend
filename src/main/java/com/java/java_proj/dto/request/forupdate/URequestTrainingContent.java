package com.java.java_proj.dto.request.forupdate;

import com.java.java_proj.entities.enums.TrainingContentDeliveryType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class URequestTrainingContent {

    private Integer id;

    @NotBlank(message = "Content name is required.")
    private String content;

    @NotNull(message = "Output standard is required.")
    private String learningObjective;

    @NotNull(message = "Delivery type is required")
    private TrainingContentDeliveryType deliveryType;

    @NotNull(message = "Training time is required")
    @Min(value = 0, message = "Training duration must be greater than 0 min.")
    private Integer duration;

    private Boolean trainingFormat;
}
