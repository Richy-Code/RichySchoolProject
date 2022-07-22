package com.example.demo.binding_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgressReport {

    private List<String> categories;

    private Map<String,List<Double>> graphData;
}
