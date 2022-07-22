package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MarksId implements Serializable {
    @OneToOne
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "student_id"
    )
    private Student student_id;

    @OneToOne
    @JoinColumn(
            name = "class_id",
            referencedColumnName = "class_id"
    )
    private Classes class_id;

    @OneToOne
    @JoinColumn(
            name = "term_id",
            referencedColumnName = "term_id"
    )
    private Term term_id;

    @OneToOne
    @JoinColumn(
            name = "subject_id",
            referencedColumnName = "subject_id"
    )
    private Subjects subject_id;
}
