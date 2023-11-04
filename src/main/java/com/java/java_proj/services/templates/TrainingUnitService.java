package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingUnit;
import com.java.java_proj.dto.request.forupdate.URequestTrainingUnit;
import com.java.java_proj.dto.response.fordetail.DResponseTrainingMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TrainingUnitService {

    void addList(String dummyCode, List<CRequestTrainingUnit> newList);

    void updateList(List<URequestTrainingUnit> updateList);

    void deleteList(List<String> deleteList);

    DResponseTrainingMaterial addMaterial(String unitCode, MultipartFile file);

}
