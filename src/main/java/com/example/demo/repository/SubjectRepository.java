package com.example.demo.repository;

import com.example.demo.entities.Department;
import com.example.demo.entities.Subjects;
import com.example.demo.entities.Teacher;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.enum_entities.SubjectOptions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends CrudRepository<Subjects,Long> {
    @Query("SELECT S FROM Subjects S WHERE S.teacher_assigned = :teacher_id")
    List<Subjects> teacherSubjects(@Param("teacher_id") Teacher teacher);

    @Query("SELECT S FROM Subjects S WHERE S.department_subject = :department AND S.subject_Status = :status")
    List<Subjects> subjectsByDepartment(@Param("department")Department department,
                                        @Param("status")Student_Status status);

    @Query("SELECT S.subject_name FROM Subjects S WHERE S.teacher_assigned = :teacher_id")
    List<String> teacherSubjectName(@Param("teacher_id") Teacher teacher);

    @Query("SELECT sub FROM Subjects sub WHERE sub.options = :opt AND sub.department_subject = :dpt")
    List<Subjects> findByOptions(@Param("opt")SubjectOptions options,@Param("dpt") Department department);
}
