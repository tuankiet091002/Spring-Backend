package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.entities.enums.ProgramStatus;

import java.time.LocalDate;

public interface LResponseTrainingProgram {
    public Integer getCode();
    public String getName();
    public LocalDate getCreatedDate();
    public LResponseUser getCreatedBy();
    public Integer getDuration();
    public ProgramStatus getStatus();
}
