package com.example.demo.binding_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SbaEntry {
    private String full_name;
    private String student_id;
    private double total;
    private double percent_of_total_sba;
    private double[] class_work;
}
