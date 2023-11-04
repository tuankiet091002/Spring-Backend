package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.ProgramStatus;

import java.time.LocalDate;
import java.util.List;

public interface DResponseSyllabusGeneral {
    public String getCode();

    public String getName();

    public ProgramStatus getPublishStatus();

    public String getVersion();

    public PermissionAccessType getLevel();

    public Integer getAttendeeNumber();

    public LocalDate getModifiedDate();

    public LResponseUser getModifiedBy(); //Format: Modified on mm/dd/yyyy by {user.full_name}

    public Integer getDuration();

    public String getCourseObjectives();

    public List<DResponseLearningObjective> getLearningObjectives();

    public String getTechnicalRequirements();
}
