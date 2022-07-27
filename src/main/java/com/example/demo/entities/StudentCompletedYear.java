package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentCompletedYear {
    @Id
    @SequenceGenerator(
            name = "year_completed",
            sequenceName = "year_completed",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "year_completed"
    )

    private Long yearCompletedId;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "student_id"
    )
    private Student student;

    public StudentCompletedYear(Student student, Academic_Year year) {
        this.student = student;
        this.year = year;
    }

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "academicYear",
            referencedColumnName = "academic_year_id"
    )
    private Academic_Year year;
}
