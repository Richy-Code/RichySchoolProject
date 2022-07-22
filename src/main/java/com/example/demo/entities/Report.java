package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private Student student;
    private String full_name;
    private double total_score;
    private int aggregate;
    private double average_score;
    private ReportDetails reportDetails;
    private String position_in_class;
    private List<SubjectReportSummary> summaryList = new ArrayList<>();
}
