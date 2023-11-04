package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import com.java.java_proj.dto.request.forcreate.CRequestTrainingUnit;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;
import com.java.java_proj.entities.drafts.SyllabusDraft;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.List;

public interface SyllabusImportService {
    public DResponseSyllabus importSyllabus(InputStream file, String scanningOption, String handleOption);

    public List<String> importLearningObjective();

    public List<CRequestTrainingUnit> importTrainingUnit();

    public List<CRequestTrainingContent> importTrainingContent();

    public String[] setSyllabusDTO(Row row);

    public String[] setLearningObjectiveDTO(Row row);

    public String[] setTrainingUnitDTO(Row row);

    public String[] setTrainingContentDTO(Row row);

    public void handleAllow(CRequestSyllabus request, String scanningOption);

    public void handleReplace(SyllabusDraft request);

    public SyllabusDraft findDuplicate(CRequestSyllabus requestSyllabus, String scanningOption);
}
