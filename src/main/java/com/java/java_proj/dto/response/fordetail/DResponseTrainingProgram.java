package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseSyllabus;
import com.java.java_proj.dto.response.forlist.LResponseUser;
import com.java.java_proj.entities.enums.ProgramStatus;

import java.time.LocalDate;
import java.util.List;

//@Getter
//@Setter
//@NoArgsConstructor
//@ToString
public interface DResponseTrainingProgram {

    public Integer getCode();

    public String getName();

    public Integer getDuration();

    public String getGeneralInformation();

    public List<DResponseTrainingMaterial> getTrainingMaterials();

    public List<LResponseSyllabus> getSyllabuses();

    public ProgramStatus getStatus();

    public LResponseUser getCreatedBy();

    public LocalDate getCreatedDate();

    public LResponseUser getModifiedBy();

    public LocalDate getModifiedDate();
}
