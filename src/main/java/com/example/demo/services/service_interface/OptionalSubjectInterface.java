package com.example.demo.services.service_interface;

import com.example.demo.entities.Department;
import com.example.demo.entities.OptionalSubject;
import com.example.demo.entities.Subjects;

import java.util.List;

public interface OptionalSubjectInterface {
    void saveOptionalSubject(OptionalSubject subject);
    List<OptionalSubject> findByDepartment(Department department);

    List<Subjects> optSubjectList(List<Long> subjectIds);

    OptionalSubject findOptSubjectById(Long subjectId);

    OptionalSubject findMainOptSubject(Long subject);

    void deleteOptionalSubjects(OptionalSubject subject);
}
