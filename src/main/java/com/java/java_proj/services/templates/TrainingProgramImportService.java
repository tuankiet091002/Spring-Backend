package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.entities.drafts.TrainingProgramDraft;

import java.io.InputStream;
import java.util.List;

public interface TrainingProgramImportService {

    List<DResponseTrainingProgram> importProgram(InputStream file, String scanningOption, String handleOption);

    public void handleAllow(CRequestTrainingProgram request, String scanningOption);

    public void handleReplace(TrainingProgramDraft duplicate);

    public TrainingProgramDraft findDuplicate(CRequestTrainingProgram requestSyllabus, String scanningOption);

}
