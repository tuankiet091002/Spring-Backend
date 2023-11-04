package com.java.java_proj.entities;

import com.java.java_proj.entities.enums.PermissionAccessType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="user_permissions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String role;

    @Column(name = "syllabus")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType syllabus;

    @Column(name = "training_program")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType trainingProgram;

    @Column(name = "class")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType classManagement;

    @Column(name = "learning_material")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType learningMaterial;

    @Column(name = "user_management")
    @Enumerated(EnumType.ORDINAL)
    private PermissionAccessType userManagement;


}
