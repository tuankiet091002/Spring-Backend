package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LRepsonseUserPermission;

import java.time.LocalDate;

public interface DResponseUser {
    public Integer getId() ;

    public String getName();

    public String getEmail();

    public String getPhone();

    public LocalDate getDob();

    public LRepsonseUserPermission getRole();

    public Boolean getGender();

    public Boolean getStatus();
}
