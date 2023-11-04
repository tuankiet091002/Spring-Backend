package com.java.java_proj.dto.response.fordetail;

import java.util.List;

public interface DResponseTrainingUnit {

    public String getCode();

    public String getName();

    public Integer getDayNumber();

    public List<DResponseTrainingContent> getTrainingContents();
}
