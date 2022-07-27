package com.example.demo.binding_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryReport {
    private String subjectName;
    private String className;

    private String termName;
    private double marks;
}
