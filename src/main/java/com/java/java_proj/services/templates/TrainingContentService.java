package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;
import com.java.java_proj.dto.request.forupdate.URequestTrainingContent;

import java.util.List;

public interface TrainingContentService {
    void addList(String dummyCode, List<CRequestTrainingContent> trainingContentList);

    void modifyList(List<URequestTrainingContent> trainingContentList);

    void deleteList(List<Integer> trainingContentList);

    void add(CRequestTrainingContent request);

}
