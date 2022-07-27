package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectReportSummary {
    private String subject_name;
    private double exams_score;
    private double class_score;
    private double total;
    private String position;
    private String remarks;
    private String grade;

}
