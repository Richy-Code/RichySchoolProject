package com.example.demo.entities;

import com.example.demo.enum_entities.CoreSubject;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.enum_entities.SubjectOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subjects {
    public Subjects(String subject_name, Department department_subject,Set<Teacher> teacher_assigned,
                    SubjectOptions subjectOptions,Student_Status subject_Status,CoreSubject coreSubject) {
        this.subject_name = subject_name;
        this.department_subject = department_subject;
        this.teacher_assigned = teacher_assigned;
        this.options = subjectOptions;
        this.subject_Status = subject_Status;
        this.coreSubject = coreSubject;
    }

    @Id
    @SequenceGenerator(
            name = "subject_sequence",
            sequenceName = "subject_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subject_sequence"
    )
    private Long subject_id;
    @Column(nullable = false)
    private String subject_name;

    @Column(nullable = false)
    private SubjectOptions options;

    @Column(nullable = false)
    private Student_Status subject_Status;

    @Column(nullable = false)
    private CoreSubject coreSubject;
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

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    private Set<Teacher> teacher_assigned;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "opt_sub_student",
            joinColumns = @JoinColumn(
                    name = "subject_id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "student_id"
            )
    )
    private Set<Student> studentSet;

    @Transient
    public String shotSubjectName() {
        if (subject_name.length() > 11) {
            String[] subsNames = subject_name.split(" ");
            if (subsNames.length == 1)
                return subject_name;
            else if (subsNames.length == 2) {
                return subsNames[0].substring(0, 3) + "." + " " + subsNames[1];
            } else {
                StringBuilder subName = new StringBuilder();
                for (String subs : subsNames) {
                    int i = 0;
                    if (subs.equalsIgnoreCase("and"))
                        continue;
                    subName.append(subs.charAt(i++));
                }
                return subName.toString();
            }
        }
        else  return subject_name;
    }
}
