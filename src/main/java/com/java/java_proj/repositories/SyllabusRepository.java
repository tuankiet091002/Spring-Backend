package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseSyllabus;
import com.java.java_proj.dto.response.fordetail.DResponseSyllabusGeneral;
import com.java.java_proj.entities.Syllabus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, String> {

    Integer countByCode(String code);

    Integer countByCodeOrName(String code, String name);

    @Query(value = "(SELECT s.*, u.name as owner_name FROM syllabuses s " +
            "LEFT JOIN users u ON s.created_by = u.id WHERE " +
            "s.publish_status = 'ACTIVE' AND " +
            "(:code = '' OR s.topic_code = :code) AND " +
            "s.name LIKE CONCAT('%',:name, '%') AND " +
            ":createdDate IS NULL OR s.created_date = :createdDate) " +
            "UNION ALL " +
            "(SELECT s.*, u.name as owner_name FROM syllabuses_draft s " +
            "LEFT JOIN users u ON s.created_by = u.id WHERE " +
            "u.id = :ownerId AND " +
            "(:code = '' OR s.topic_code = :code) AND " +
            "s.name LIKE CONCAT('%',:name, '%') AND " +
            ":createdDate IS NULL OR s.created_date = :createdDate) " +
            "ORDER BY " +
            "CASE WHEN :orderPhrase = 'name ASC' THEN name END, " +
            "CASE WHEN :orderPhrase = 'name DESC' THEN name END DESC, " +
            "CASE WHEN :orderPhrase = 'code ASC' THEN topic_code END, " +
            "CASE WHEN :orderPhrase = 'code DESC' THEN topic_code END DESC, " +
            "CASE WHEN :orderPhrase = 'createdDate ASC' THEN created_date END, " +
            "CASE WHEN :orderPhrase = 'createdDate DESC' THEN created_date END DESC, " +
            "CASE WHEN :orderPhrase = 'createdBy ASC' THEN owner_name END, " +
            "CASE WHEN :orderPhrase = 'createdBy DESC' THEN owner_name END DESC, " +
            "CASE WHEN :orderPhrase = 'duration ASC' THEN duration END, " +
            "CASE WHEN :orderPhrase = 'duration DESC' THEN duration END DESC ",
            countQuery = "SELECT (SELECT count(*) FROM syllabuses s WHERE " +
                    "s.publish_status = 'ACTIVE' AND " +
                    "(:code = '' OR s.topic_code = :code) AND " +
                    "s.name LIKE CONCAT('%',:name, '%') AND " +
                    ":createdDate IS NULL OR s.created_date = :createdDate) " +
                    " + " +
                    "(SELECT count(*) FROM syllabuses_draft s WHERE " +
                    "s.publish_status = 'ACTIVE' AND " +
                    "(:code = '' OR s.topic_code = :code) AND " +
                    "s.name LIKE CONCAT('%',:name, '%') AND " +
                    ":createdDate IS NULL OR s.created_date = :createdDate) "
            , nativeQuery = true)
    Page<Syllabus> findByQuery(String code, String name, LocalDate createdDate,
                               String orderPhrase, Pageable pageable, Integer ownerId);

    @Query("SELECT s FROM Syllabus s JOIN FETCH s.topicOutline WHERE s.code = :code")
    Optional<Syllabus> duplicateSyllabusByCode(@Param("code") String code);

    @Query("SELECT s FROM Syllabus s JOIN FETCH s.topicOutline WHERE s.code = :code")
    DResponseSyllabus findSyllabusByCode(@Param("code") String code);

    DResponseSyllabusGeneral findSyllabusGeneralByCode(String code);

}
