package com.example.demo.repository;

import com.example.demo.entities.Department;
import com.example.demo.entities.Subjects;
import com.example.demo.entities.Teacher;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.enum_entities.SubjectOptions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface SubjectRepository extends CrudRepository<Subjects,Long> {
    @Query("SELECT S FROM Subjects S INNER JOIN S.teacher_assigned T WHERE T.teacher_id = :teacher_id")
    List<Subjects> teacherSubjects(@Param("teacher_id") Long teacherId);

    @Query("SELECT S FROM Subjects S WHERE S.department_subject = :department AND S.subject_Status = :status")
    List<Subjects> subjectsByDepartment(@Param("department")Department department,
                                        @Param("status")Student_Status status);

    @Query("SELECT S.subject_name FROM Subjects S INNER JOIN S.teacher_assigned T WHERE T.teacher_id = :teacher_id")
    List<String> teacherSubjectName(@Param("teacher_id") Long teacherId);

    @Query("SELECT sub FROM Subjects sub WHERE sub.options = :opt AND sub.department_subject = :dpt " +
            "AND sub.subject_Status = :status")
    List<Subjects> findByOptions(@Param("opt")SubjectOptions options,@Param("dpt") Department department,
                                 @Param("status")Student_Status status);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Subjects s SET s.teacher_assigned = :teacher,s.subject_name = :name WHERE s.subject_id = :subId")
    void updateSubject(@Param("teacher") Set<Teacher> set, @Param("name")String name,
                       @Param("subId")Long subjectId);
}
