package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseLearningObjective;
import com.java.java_proj.entities.enums.TrainingContentDeliveryType;

public interface DResponseTrainingContent {

    public Integer getId();

    public String getContent();

    public LResponseLearningObjective getLearningObjective();

    public TrainingContentDeliveryType getDeliveryType();

    public Integer getDuration();

    public Boolean getTrainingFormat();

}
