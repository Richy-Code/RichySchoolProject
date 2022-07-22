package com.example.demo.entities;

import lombok.*;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
public class Department {
    @Id
    @SequenceGenerator(
            name = "department_sequence",
            sequenceName = "department_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "department_sequence"
    )
    private Long departmentID;
    @Column(nullable = false)
    private String department_name;

    public Department(String department_name ,DepartmentHead departmentHead) {
        this.department_name = department_name;
        this.head = departmentHead;
    }

    public Department(String department_name) {
        this.department_name = department_name;
    }

    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.LAZY
    )@JoinColumn(
            name = "department_head",
            referencedColumnName = "head_id"
    )
    private DepartmentHead head;
}
