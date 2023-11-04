package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.TrainingContentDeliveryType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "training_contents")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TrainingContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "unit_code")
    private TrainingUnit trainingUnit;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "learning_objective", nullable = false)
    private LearningObjective learningObjective;

    @Column(name = "delivery_type")
    @Enumerated(EnumType.STRING)
    private TrainingContentDeliveryType deliveryType;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "training_format", columnDefinition = "boolean default false")
    private Boolean trainingFormat;

}
