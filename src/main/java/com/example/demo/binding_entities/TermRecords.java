package com.example.demo.binding_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TermRecords {
    private String term;
    private double mark;

    public TermRecords(String term, double mark) {
        this.term = term;
        this.mark = mark;
    }

    public TermRecords(String term, List<Double> graphData) {
        this.term = term;
        this.graphData = graphData;
    }

    private List<Double> graphData;
}
