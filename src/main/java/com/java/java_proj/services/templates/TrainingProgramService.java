package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingProgram;
import com.java.java_proj.dto.request.forupdate.URequestTrainingProgram;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.dto.response.forlist.LResponseTrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface TrainingProgramService {

    Page<LResponseTrainingProgram> findAllByCodeAndName(Integer code, String name, String orderBy, Integer page,
                                                        Integer size, String orderDirection);

    DResponseTrainingProgram viewDetail(int code);

    DResponseTrainingProgram add(CRequestTrainingProgram trainingProgram);

    DResponseTrainingProgram update(URequestTrainingProgram trainingProgram);

    DResponseTrainingProgram toggle(Integer code);

    DResponseTrainingMaterial addMaterial(Integer programCode, MultipartFile file);

}
