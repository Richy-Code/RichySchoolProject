package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exams_mark_record")
public class Record_Exams {
    @Id
    @SequenceGenerator(
            name = "recordSub_sequence",
            sequenceName = "recordSub_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recordSub_sequence"
    )
    private Long exams_record_id;
    @Embedded
    private MarksId exams_score_id;
    @Column(nullable = false)
    private double marks;
    @Column(nullable = false)
    private String  position;
    @Column(nullable = false)
    private Long academic_yearId;

    public Record_Exams(MarksId exams_score_id, double marks, String position, Long academic_yearId) {
        this.exams_score_id = exams_score_id;
        this.marks = marks;
        this.position = position;
        this.academic_yearId = academic_yearId;
    }
}
