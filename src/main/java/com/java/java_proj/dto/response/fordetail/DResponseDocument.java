package com.java.java_proj.dto.response.fordetail;

import com.java.java_proj.dto.response.forlist.LResponseUser;

import java.time.LocalDate;

public interface DResponseDocument {

    public String getId();

    public String getName();

    public String getDescription();

    public String getFilename();

    public String getUrl();

    public LResponseUser getCreatedBy();

    public LocalDate getCreatedDate();
}
