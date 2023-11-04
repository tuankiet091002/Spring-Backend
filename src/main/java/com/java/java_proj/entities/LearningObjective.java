package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.ObjectiveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "learning_objectives")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LearningObjective {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ObjectiveType type;

    @Column(name = "description")
    private String description;

    @Override
    public String toString() {
        return "LearningObjective{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
