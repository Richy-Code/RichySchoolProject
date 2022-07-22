package com.example.demo.binding_entities;

import com.example.demo.entities.OptionalSubjectStudents;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OptionalSubjectStudentSet {
    List<OptionalSubjectStudents> subjectStudents;
}
