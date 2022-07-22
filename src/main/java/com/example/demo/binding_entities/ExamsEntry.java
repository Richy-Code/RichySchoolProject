package com.example.demo.binding_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamsEntry {
    private String studentId;
    private String studentFull_name;
    private double total_sba;
    private double examsScore;
    private double total_exam_marks;
    private double percent_of_total_exams;
    private String grade;
    private String position;
}
