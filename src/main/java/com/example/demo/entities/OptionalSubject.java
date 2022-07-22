package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OptionalSubject {
    @Id
    @SequenceGenerator(
            name = "opt_sequence",
            sequenceName = "opt_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "opt_sequence"
    )
    private Long opt_sub_id;

    @Column(nullable = false)
    private String opt_sub_name;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "department_subject",
            referencedColumnName = "departmentID"
    )
    private Department department_subject;

    @ManyToMany(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "opt_subject",
            joinColumns = @JoinColumn(
                    name = "opt_sub_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "subject_id"
            )
    )
    private Set<Subjects> subjectsSet;

    public OptionalSubject(String opt_sub_name, Department department_subject, Set<Subjects> subjectsSet) {
        this.opt_sub_name = opt_sub_name;
        this.department_subject = department_subject;
        this.subjectsSet = subjectsSet;
    }
}
