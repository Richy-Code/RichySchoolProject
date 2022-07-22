package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exams_mark")
public class ExamsScore {
    @EmbeddedId
    private MarksId exams_score_id;
    @Column(nullable = false)
    private double marks;
    @Column(nullable = false)
    private String  position;
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "academic_year",
            referencedColumnName = "academic_year_id"
    )
    private Academic_Year academic_year;
}
