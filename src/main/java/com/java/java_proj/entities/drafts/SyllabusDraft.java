package com.java.java_proj.entities.drafts;

import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.PermissionAccessType;
import com.java.java_proj.entities.enums.ProgramStatus;
import com.java.java_proj.exceptions.HttpException;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "syllabuses_draft")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SyllabusDraft {

    @Id
    @Column(name = "topic_code", length = 10)
    private String code;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    private PermissionAccessType level;

    @Column(name = "version", columnDefinition = "VARCHAR(60) default '1.0.0'")
    private String version = "1.0.0";

    @Column(name = "training_audience")
    private Integer attendeeNumber;

    @Column(name = "publish_status")
    @Enumerated(EnumType.STRING)
    private ProgramStatus publishStatus;

    @Column(name = "technical_requirements", columnDefinition = "TEXT")
    private String technicalRequirements;

    @Column(name = "course_objectives", columnDefinition = "TEXT")
    private String courseObjectives;

    // coi lai cascade
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "syllabus_draft_objective", joinColumns = @JoinColumn(name = "topic_code"),
            inverseJoinColumns = @JoinColumn(name = "objective_code"))
    private List<LearningObjective> learningObjectives;

    @OneToMany(mappedBy = "syllabus")
    private List<TrainingUnitDraft> topicOutline;

    @Column(name = "quiz")
    private Integer quiz;

    @Column(name = "assignment")
    private Integer assignment;

    @Column(name = "final_theory")
    private Integer finalTheory;

    @Column(name = "final_practice")
    private Integer finalPractice;

    @Column(name = "final")
    private Integer finalTotal;

    @Column(name = "gpa")
    private Integer gpa;

    @Column(name = "training_principles", columnDefinition = "TEXT")
    private String trainingPrinciples;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

    @Column(name = "duration")
    @ColumnDefault("0")
    private Integer duration = 0;

    public void updateMainVer(int mainVer) {
        // to array of string
        String[] currentVersion = this.version.split("\\.");

        // check for legit version
        if (mainVer <= Integer.parseInt(currentVersion[0]))
            throw new HttpException(HttpStatus.BAD_REQUEST, "New version must be higher than current version.");
        // reset i2 and i3
        this.version = mainVer + ".0.0";
    }

    public void nextPublish() {
        String[] currentVersion = this.version.split("\\.");
        // increase by 1
        this.version = currentVersion[0] + "." + (Integer.parseInt(currentVersion[1]) + 1) + "." + currentVersion[2];
    }

    public void nextDraft() {
        String[] currentVersion = this.version.split("\\.");
        // increase by 1
        this.version = currentVersion[0] + "." + currentVersion[1] + "." + (Integer.parseInt(currentVersion[2]) + 1);
    }

    public Integer fetchMainVersion() {
        String[] currentVersion = this.version.split("\\.");
        return Integer.parseInt(currentVersion[0]);
    }

}