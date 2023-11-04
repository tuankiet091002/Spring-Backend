package com.java.java_proj.dto.request.forupdate;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class URequestTrainingUnit {

    private String code;

    @NotNull(message = "Unit name is required.")
    private String name;

    @NotNull(message = "Day number is required.")
    private Integer dayNumber;

    private List<@Valid CRequestTrainingContent> trainingContentsAdd = new ArrayList<>();

    private List<@Valid URequestTrainingContent> trainingContentsUpdate = new ArrayList<>();

    private List<Integer> trainingContentsDelete = new ArrayList<>();
}
