package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ParentClass {

    @SequenceGenerator(
            name = "parent_sequence",
            sequenceName = "parent_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "parent_sequence"
    )
    @Id
    private Long parentClassId;

    @Column(nullable = false)
    private String parentClassName;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "class_department",
            referencedColumnName = "departmentID"
    )
    private Department class_department;

    public ParentClass(String parentClassName, Department class_department){
        this.parentClassName = parentClassName;
        this.class_department = class_department;
    }
}
