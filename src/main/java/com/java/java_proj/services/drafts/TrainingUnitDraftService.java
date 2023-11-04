package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingUnit;

import java.util.List;

public interface TrainingUnitDraftService {

    void addList(String dummyCode, List<CRequestTrainingUnit> newList);

    void deleteList(List<String> deleteList);

}
