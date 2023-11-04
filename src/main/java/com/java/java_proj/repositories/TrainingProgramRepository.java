package com.java.java_proj.repositories;

import com.java.java_proj.dto.response.fordetail.DResponseTrainingProgram;
import com.java.java_proj.entities.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, Integer> {

    @Query("select t from TrainingProgram t join fetch t.syllabuses where t.code = :id")
    DResponseTrainingProgram findOneByCode(Integer id);

    @Query(value = "SELECT p.*, u.name as owner_name FROM training_programs p " +
            "LEFT JOIN users u ON p.created_by = u.id WHERE " +
            "p.status = 'ACTIVE' AND " +
            "(:code = 0 OR p.code = :code) AND " +
            "p.name LIKE CONCAT('%',:name, '%') " +
            "UNION ALL " +
            "SELECT p.*, u.name as owner_name FROM training_programs_draft p " +
            "LEFT JOIN users u ON p.created_by = u.id WHERE " +
            "u.id = :ownerId AND " +
            "(:code = 0 OR p.code = :code) AND " +
            "p.name LIKE CONCAT('%',:name, '%') " +
            "ORDER BY " +
            "CASE WHEN :orderPhrase = 'name ASC' THEN name END, " +
            "CASE WHEN :orderPhrase = 'name DESC' THEN name END DESC, " +
            "CASE WHEN :orderPhrase = 'code ASC' THEN code END, " +
            "CASE WHEN :orderPhrase = 'code DESC' THEN code END DESC, " +
            "CASE WHEN :orderPhrase = 'createdDate ASC' THEN created_date END, " +
            "CASE WHEN :orderPhrase = 'createdDate DESC' THEN created_date END DESC, " +
            "CASE WHEN :orderPhrase = 'createdBy ASC' THEN owner_name END, " +
            "CASE WHEN :orderPhrase = 'createdBy DESC' THEN owner_name END DESC, " +
            "CASE WHEN :orderPhrase = 'duration ASC' THEN duration END, " +
            "CASE WHEN :orderPhrase = 'duration DESC' THEN duration END DESC ",
            countQuery = "SELECT (SELECT count(*) FROM training_programs p WHERE " +
                    "p.status = 'ACTIVE' AND " +
                    "(:code = 0 OR p.code = :code) AND " +
                    "p.name LIKE CONCAT('%',:name, '%')) " +
                    "+ " +
                    "(SELECT count(*) FROM training_programs_draft p WHERE " +
                    "p.status = 'ACTIVE' AND " +
                    "(:code = 0 OR p.code = :code) AND " +
                    "p.name LIKE CONCAT('%',:name, '%')) ",
            nativeQuery = true)
    Page<Object[]> findByQuery(Integer code, String name, String orderPhrase,
                               Pageable pageable, Integer ownerId);
}