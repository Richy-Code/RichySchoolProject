package com.example.demo.binding_entities;

import com.example.demo.entities.Subjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectSet {
    private List<Subjects> subSubjectList;
    private String optSubjectName;
    Long optDepartmentId;
}
