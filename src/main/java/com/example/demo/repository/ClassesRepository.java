package com.example.demo.repository;

import com.example.demo.entities.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ClassesRepository extends CrudRepository<Classes, String> {
    @Query("SELECT cls FROM Classes cls WHERE cls.parentClass.class_department = :department AND " +
            "cls.class_teacher_id IS NOT NULL")
    List<Classes> findClassByDepartment(@Param("department")Department department);

    @Query("SELECT C.class_name FROM Classes C INNER JOIN C.class_teachers T WHERE " +
            "T.teacher_id = :teacher")
    List<String>findClassesByTeacher(@Param("teacher")Long teacherId);

    @Query("SELECT C FROM Classes C INNER JOIN C.class_teachers T WHERE " +
            "T.teacher_id = :teacher")
    List<Classes> findTeacherClasses(@Param("teacher") Long teacherId);
    @Query("SELECT cls FROM Classes cls WHERE cls.parentClass.class_department.head = :head AND " +
            "cls.class_teacher_id IS NOT NULL")
    List<Classes> listClassesByDepartmentHead(@Param("head")DepartmentHead head);

    @Query("SELECT cls FROM Classes cls WHERE cls.parentClass = :parent")
    List<Classes> listClassesByParentClass(@Param("parent")ParentClass parentClass);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Classes cls SET cls.class_name = :name WHERE cls.class_id = :id")
    void updateSubClassName(@Param("name")String className,@Param("id")String classId);


}
