package com.java.java_proj.services.templates;

import com.java.java_proj.dto.request.forcreate.CRequestSyllabus;
import com.java.java_proj.dto.request.forupdate.URequestSyllabus;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabusGeneral;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabusOthers;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabusOutline;
import com.java.java_proj.dto.response.forlist.LResponseSyllabus;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface SyllabusService {

    Page<LResponseSyllabus> findAllByCodeAndNameAndCreatedDate(String code, String name, LocalDate createdDate,
                                                               String orderBy, String orderDirection,
                                                               Integer page, Integer size);

    DResponseSyllabus getSyllabusDetail(String code);

    String generateCode(String name);

    DResponseSyllabus add(CRequestSyllabus syllabus);

    DResponseSyllabus update(URequestSyllabus syllabus);

    DResponseSyllabus toggle(String code);

    DResponseSyllabus getSyllabusDuplicate(String code);

    DResponseSyllabusGeneral getSyllabusGeneral(String topicCode);

    DResponseSyllabusOutline getSyllabusOutline(String topicCode);

    DResponseSyllabusOthers getSyllabusOthers(String topicCode);
}
