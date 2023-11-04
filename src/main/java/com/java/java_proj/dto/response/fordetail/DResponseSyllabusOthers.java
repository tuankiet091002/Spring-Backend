package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.entities.enums.TrainingContentDeliveryType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
public class DResponseSyllabusOthers {
    @Getter
    private String code;
    @Getter
    private Integer quiz;
    @Getter
    private Integer assignment;
    @Getter
    private Integer finalTotal;
    @Getter
    private Integer finalTheory;
    @Getter
    private Integer finalPractice;
    @Getter
    private String trainingPrinciples;

    private List<DResponseTrainingUnit> timeAllocation;

    public HashMap<String, Integer> getTimeAllocation() {
        HashMap<String, Integer> map = new HashMap<>();
        for (TrainingContentDeliveryType trainingType : TrainingContentDeliveryType.values()) {
            map.put(trainingType.name(), 0);
        }

        this.timeAllocation.forEach(unit -> unit.getTrainingContents()
                .forEach(content -> map.merge(content.getDeliveryType().toString(), content.getDuration(), Integer::sum)));
        return map;
    }
}
