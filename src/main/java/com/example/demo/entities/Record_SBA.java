package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sab_marks_record")
public class Record_SBA {
    @Id
    @SequenceGenerator(
            name = "recordExam_sequence",
            sequenceName = "recordExam_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recordExam_sequence"
    )
    private Long sba_mark_id;
    @Embedded
    private MarksId marksId;
    @ElementCollection(fetch = FetchType.EAGER)
    List<Double> marks = new ArrayList<>();
    @Column(nullable = false)
    private Long academic_yearId;

    public Record_SBA(MarksId marksId, List<Double> marks, Long academic_yearId) {
        this.marksId = marksId;
        this.marks = marks;
        this.academic_yearId = academic_yearId;
    }
}
