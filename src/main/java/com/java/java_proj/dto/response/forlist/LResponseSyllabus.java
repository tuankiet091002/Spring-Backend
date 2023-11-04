package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.entities.enums.ProgramStatus;

import java.time.LocalDate;
import java.util.List;

public interface LResponseSyllabus {
    public String getName();

    public String getCode();

    public LocalDate getCreatedDate();

    public LResponseUser getCreatedBy();

    public List<LResponseLearningObjective> getLearningObjectives();

    public ProgramStatus getPublishStatus();

    public Integer getDuration();

}
