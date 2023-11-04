package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.ProgramStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_programs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer code;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "duration", nullable = false)
    private Integer duration = 0;

    @Column(name = "general_infomation", nullable = false)
    private String generalInformation;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "training_program_materials",
            joinColumns = @JoinColumn(name = "training_program_code"),
            inverseJoinColumns = @JoinColumn(name = "training_material_code"))
    private List<TrainingMaterial> trainingMaterials = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "training_program_syllabuses",
            joinColumns = @JoinColumn(name = "training_program_code"),
            inverseJoinColumns = @JoinColumn(name = "syllabus_code"))
    private List<Syllabus> syllabuses = new ArrayList<>();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Column(name = "modified_date")
    private LocalDate modifiedDate;

}
