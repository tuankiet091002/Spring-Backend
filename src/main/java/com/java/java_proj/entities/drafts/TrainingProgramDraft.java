package com.java.java_proj.entities.drafts;

import com.java.java_proj.entities.Syllabus;
import com.java.java_proj.entities.TrainingMaterial;
import com.java.java_proj.entities.User;
import com.java.java_proj.entities.enums.ProgramStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_programs_draft")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingProgramDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private Integer duration = 0;

    @Column(name = "general_infomation")
    private String generalInformation;

    @ManyToMany
    @JoinTable(name = "training_program_draft_syllabuses",
            joinColumns = @JoinColumn(name = "training_program_code"),
            inverseJoinColumns = @JoinColumn(name = "syllabus_code"))
    private List<Syllabus> syllabuses = new ArrayList<>();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

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


    public List<TrainingMaterial> getTrainingMaterials() {
        return new ArrayList<>();
    }
}
