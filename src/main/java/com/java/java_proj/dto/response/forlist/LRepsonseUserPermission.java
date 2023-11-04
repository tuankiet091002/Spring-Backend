package com.java.java_proj.dto.response.forlist;

import com.java.java_proj.entities.enums.PermissionAccessType;

public interface LRepsonseUserPermission {
    public Integer getId();
    public String getRole();
    public PermissionAccessType getSyllabus();
    public PermissionAccessType getTrainingProgram();
    public PermissionAccessType getClassManagement();
    public PermissionAccessType getLearningMaterial();
    public PermissionAccessType getUserManagement();


}
