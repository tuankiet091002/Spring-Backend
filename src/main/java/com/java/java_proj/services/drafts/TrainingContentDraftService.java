package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestTrainingContent;

import java.util.List;

public interface TrainingContentDraftService {

    void addList(String dummyCode, List<CRequestTrainingContent> trainingContentList);

    void deleteList(List<Integer> trainingContentList);
}
