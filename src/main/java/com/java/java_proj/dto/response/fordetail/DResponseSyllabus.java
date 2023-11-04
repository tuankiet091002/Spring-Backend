package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.ProgramStatus;

import java.time.LocalDate;
import java.util.List;

public interface DResponseSyllabus {
    public String getCode();

    public String getName();

    public PermissionAccessType getLevel();

    public String getTechnicalRequirements();

    public String getVersion();

    public Integer getDuration();

    public Integer getAttendeeNumber();

    public String getTrainingPrinciples();

    public ProgramStatus getPublishStatus();

    public String getCourseObjectives();

    public List<DResponseLearningObjective> getLearningObjectives();  // courseObjectives.description // ($?)

    public List<DResponseTrainingUnit> getTopicOutline();

    public Integer getQuiz();

    public Integer getAssignment();

    public Integer getFinalTheory();

    public Integer getFinalPractice();

    public Integer getFinalTotal();

    public Integer getGpa();

    public LResponseUser getCreatedBy();

    public LocalDate getCreatedDate();

    public LResponseUser getModifiedBy();

    public LocalDate getModifiedDate();

}
