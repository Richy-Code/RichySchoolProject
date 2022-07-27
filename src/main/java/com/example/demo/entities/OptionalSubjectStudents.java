package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionalSubjectStudents {
    private Student student;
    private Long optSubjectId;
    public OptionalSubjectStudents(Student student) {
        this.student = student;
    }
}
