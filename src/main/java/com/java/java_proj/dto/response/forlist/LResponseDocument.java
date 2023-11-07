package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;

import java.time.LocalDate;

public interface LResponseDocument {

    public String getId();

    public String getName();

    public LResponseUser getCreatedBy();

    public LocalDate getCreatedDate();
}
