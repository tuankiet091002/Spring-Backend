package com.java.java_proj.entities.drafts;

import com.java.java_proj.entities.LearningObjective;
import com.java.java_proj.entities.enums.TrainingContentDeliveryType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "training_contents_draft")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingContentDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "training_unit")
    private TrainingUnitDraft trainingUnit;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "learning_objective")
    private LearningObjective learningObjective;

    @Column(name = "delivery_type")
    @Enumerated(EnumType.STRING)
    private TrainingContentDeliveryType deliveryType;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "training_format", columnDefinition = "boolean default false")
    private Boolean trainingFormat;

}
