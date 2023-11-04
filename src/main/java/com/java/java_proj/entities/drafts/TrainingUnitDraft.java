package com.java.java_proj.entities.drafts;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "training_units_draft")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingUnitDraft {

    @Id
    @Column(name = "unit_code")
    private String code;

    @Column(name = "unit_name")
    private String name;

    @Column(name = "day_number")
    private Integer dayNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_code")
    private SyllabusDraft syllabus;

    @OneToMany(mappedBy = "trainingUnit", fetch = FetchType.EAGER)
    private List<TrainingContentDraft> trainingContents;

    public Integer getDuration() {
        return this.getTrainingContents().stream()
                .mapToInt(TrainingContentDraft::getDuration).sum();
    }
}
