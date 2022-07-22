package com.example.demo.binding_entities;

import com.example.demo.entities.ExamsScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamsScoreSet {
    private List<ExamsScore> examsScores;

}
