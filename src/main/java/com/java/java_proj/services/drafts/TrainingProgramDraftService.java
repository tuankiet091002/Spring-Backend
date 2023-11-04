package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.request.forupdate.URequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;

public interface TrainingProgramDraftService {

    DResponseTrainingProgram getProgramDetail(Integer code);

    DResponseTrainingProgram add(CRequestTrainingProgram trainingProgram);

    DResponseTrainingProgram update(URequestTrainingProgram trainingProgram);

    void delete(Integer code);
}
