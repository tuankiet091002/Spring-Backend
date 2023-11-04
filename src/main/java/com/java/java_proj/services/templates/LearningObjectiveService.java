package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestLearningObjective;
import com.java.java_proj.dto.request.forupdate.URequestLearningObjective;
import com.java.java_proj.dto.response.fordetail.DResponseLearningObjective;

import java.util.List;

public interface LearningObjectiveService {

    List<DResponseLearningObjective> getByCodeAndName(String code, String name);

    DResponseLearningObjective add(CRequestLearningObjective requestLearningObjective);

    DResponseLearningObjective update(URequestLearningObjective requestLearningObjective);
}
