package com.example.demo.services.service_interface;

import com.example.demo.entities.Department;
import com.example.demo.entities.Subjects;
import com.example.demo.entities.Teacher;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.enum_entities.SubjectOptions;

import java.util.List;

public interface SubjectInterface {
    void saveSubject(Subjects subjects);
    void saveAllSubject(List<Subjects> subjects);
    Subjects findSubjectById(Long id);

    List<Subjects> subjectsList();

    List<Subjects> findSubjectByTeacher(Teacher teacher);
    List<Subjects> subjectByDepartment(Department department, Student_Status status);

    List<String> teacherSubjectName(Teacher teacher);

    List<Subjects> subjectByOptionAndDepartment(SubjectOptions options, Department department,
                                                Student_Status status);
}
