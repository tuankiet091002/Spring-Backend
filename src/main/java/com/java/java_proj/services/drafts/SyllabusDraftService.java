package com.java.java_proj.services.drafts;

import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;

public interface SyllabusDraftService {

    DResponseSyllabus getSyllabusDetail(String code);

    DResponseSyllabus add(CRequestSyllabus syllabus);

    void delete(String code);
}
