package com.example.demo.binding_entities;

import com.example.demo.entities.ExamsScore;
import com.example.demo.entities.SBA;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TempSubjectSummary {
    private ExamsScore examsScore;
    private SBA classScore;
}
