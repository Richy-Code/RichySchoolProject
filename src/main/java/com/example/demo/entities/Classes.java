package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Classes {
    @Id
    @GeneratedValue(generator = "cls-generator")
    @GenericGenerator(name = "cls-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "class"),
            strategy = "com.example.demo.entities.ClassKeyGenerator")
    private String class_id;
    @Column(nullable = false)
    private String class_name;
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "class_teacher_id",
            referencedColumnName = "teacher_id"
    )
    private Teacher class_teacher_id;

    public Classes(String class_name, Teacher class_teacher_id,
                   ParentClass parentClass,Set<Teacher> teachers) {
        this.class_name = class_name;
        this.class_teacher_id = class_teacher_id;
        this.parentClass = parentClass;
        this.class_teachers = teachers;

    }

    public Classes(String class_name, ParentClass parentClass) {
        this.class_name = class_name;
        this.parentClass = parentClass;
    }
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "parent_class",
            referencedColumnName = "parentClassId"
    )
    private ParentClass parentClass;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "classes_teacher",
            joinColumns = @JoinColumn(
                    name = "class_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "teacher_id"
            )
    )
    private Set<Teacher> class_teachers;

    @Transient
    List<Long> selectedTeachers;
}
