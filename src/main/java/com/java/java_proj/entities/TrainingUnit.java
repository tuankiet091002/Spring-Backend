package com.java.java_proj.entities;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "training_units")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingUnit {

    @Id
    @Column(name = "unit_code")
    private String code;

    @Column(name = "unit_name")
    private String name;

    @Column(name = "day_number")
    private Integer dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_code")
    private Syllabus syllabus;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "training_unit_materials",
            joinColumns = @JoinColumn(name = "training_unit_code"),
            inverseJoinColumns = @JoinColumn(name = "training_material_code"))
    private List<TrainingMaterial> trainingMaterials;

    @OneToMany(mappedBy = "trainingUnit", fetch = FetchType.EAGER)
    private List<TrainingContent> trainingContents;

    public Integer getDuration() {
        return this.getTrainingContents().stream()
                .mapToInt(TrainingContent::getDuration).sum();
    }
}
