package com.example.demo.repository;

import com.example.demo.entities.Department;
import com.example.demo.entities.Teacher;
import com.example.demo.entities.Users;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TeacherRepository extends CrudRepository<Teacher,Long> {
    @Query("SELECT teach FROM Teacher teach WHERE teach.users = :user")
    Teacher findTeacherByUser(@Param("user")Users users);

    @Query("SELECT tch FROM Teacher tch WHERE tch.department_Id = :department AND tch.status = :status")
    List<Teacher> teacherByDepartment(@Param("department")Department department, @Param("status")Student_Status status);

    @Query("SELECT tch FROM Teacher tch WHERE tch.first_name=:first AND tch.middle_name = :middle AND " +
            "tch.last_name = :last")
    Teacher teacherByNames(@Param("first")String first,@Param("middle")String middle, @Param("last")String last);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Teacher t SET t.status = :status,t.last_name=:last,t.first_name = :first,t.contact=:cont" +
            ", t.middle_name = :mid,t.department_Id = :dpt,t.gender = :gen,t.photo = :photo WHERE t.teacher_id" +
            "= :id")
    void updateTeacher(@Param("status")Student_Status status, @Param("last")String lastName, @Param("first")
                       String firstName, @Param("cont")String cont, @Param("mid")String middleName,
                       @Param("dpt")Department department, @Param("gen")Gender gender,@Param("photo")
                       String photo,@Param("id")Long id);

    @Query(value = "SELECT * FROM teacher WHERE department_id =:dpt AND teacher_id NOT IN (" +
            "SELECT class_teacher_id FROM classes)",nativeQuery = true)
    List<Teacher> possibleClassTeachers(@Param("dpt")Department department);
}
