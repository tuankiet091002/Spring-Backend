package com.java.java_proj.dto.request.forcreate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CRequestSyllabus {

    @NotBlank(message = "Code is required.")
    private String code;

    @NotBlank(message = "Syllabus name is required.")
    @Size(max = 60, message = "Syllabus name must be less than 60 characters.")
    private String name;

    @NotBlank(message = "Level is required.")
    @Pattern(regexp = "^(AUTO|ACCESS_DENIED|VIEW|MODIFY|CREATE|FULL_ACCESS)$",
            message = "Wrong \"level\" format, valid value include ACCESS_DENIED, VIEW, MODIFY, CREATE, FULL_ACCESS")
    private String level;

    @NotNull(message = "Attendee number is required.")
    @Min(0)
    private Integer attendeeNumber;

    @NotBlank(message = "Technical requirement(s) is required. ")
    private String technicalRequirements;

    @NotBlank(message = "Course objective(s) is required.")
    private String courseObjectives;

    @NotEmpty(message = "Learning objectives are required.")
    private List<String> learningObjectives;

    @NotEmpty(message = "Please input at least one day.")
    private List<@Valid CRequestTrainingUnit> topicOutlineAdd;

    private String trainingPrinciples;

    @NotNull(message = "Quiz is required.")
    @Min(value = 0, message = "Quiz must higher than 0% total.")
    @Max(value = 100, message = "Quiz must less than 100% total.")
    private Integer quiz;

    @NotNull(message = "Assignment is required.")
    @Min(value = 0, message = "Assignment must higher than 0% total.")
    @Max(value = 100, message = "Assignment must less than 100% total.")
    private Integer assignment;

    @NotNull(message = "Final Theory is required.")
    @Min(value = 0, message = "Final Theory must higher than 0% total.")
    @Max(value = 100, message = "Final Theory must less than 100% total.")
    private Integer finalTheory;

    @NotNull(message = "Final Practice is required.")
    @Min(value = 0, message = "Final Practice must higher than 0% total.")
    @Max(value = 100, message = "Final Practice must less than 100% total.")
    private Integer finalPractice;

    @NotNull(message = "Final is required.")
    @Min(value = 0, message = "Final must higher than 0% total.")
    @Max(value = 100, message = "Final must less than 100% total.")
    private Integer finalTotal;

    @NotNull(message = "GPA is required.")
    @Min(value = 0, message = "GPA must higher than 0% total.")
    @Max(value = 100, message = "GPA must less than 100% total.")
    private Integer gpa;

    public CRequestSyllabus(String code,
                            String name,
                            String level,
                            Integer attendeeNumber,
                            String technicalRequirements,
                            String courseObjectives,
                            String trainingPrinciples,
                            Integer quiz,
                            Integer assignment,
                            Integer finalTheory,
                            Integer finalPractice,
                            Integer finalTotal,
                            Integer gpa) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.attendeeNumber = attendeeNumber;
        this.technicalRequirements = technicalRequirements;
        this.courseObjectives = courseObjectives;
        this.trainingPrinciples = trainingPrinciples;
        this.quiz = quiz;
        this.assignment = assignment;
        this.finalTheory = finalTheory;
        this.finalPractice = finalPractice;
        this.finalTotal = finalTotal;
        this.gpa = gpa;
    }


}
