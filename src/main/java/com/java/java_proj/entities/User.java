package com.java.java_proj.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 20, nullable = false, unique = true)
    private String phone;

    @Column(name = "dob", nullable = false, columnDefinition = "DATE")
    private LocalDate dob;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role")
    private UserPermission role;

    @Column(name = "gender", columnDefinition = "boolean default true")
    private Boolean gender;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_date", columnDefinition = "DATE")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by")
    private User modifiedBy;

    @Column(name = "modified_date", columnDefinition = "DATE")
    private LocalDate modifiedDate;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private List<TrainingProgram> trainingPrograms;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<RefreshToken> refreshToken;


    @Override
    public String toString() {
        return "User{ " +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", role=" + role +
                ", gender=" + gender +
                ", status=" + status + " " +
                '}';
    }
}
