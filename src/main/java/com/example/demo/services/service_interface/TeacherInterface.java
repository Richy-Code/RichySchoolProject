package com.example.demo.services.service_interface;

import com.example.demo.entities.Department;
import com.example.demo.entities.Teacher;
import com.example.demo.entities.Users;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;

import java.util.List;

public interface TeacherInterface {

    void saveTeacher(Teacher teacher);

    void saveAllTeachers(List<Teacher> teacherList);

    List<Teacher> findAllTeachers();

    Teacher findTeacherByID(Long Id);

    Teacher findTeacherByUsers(Users users);

    List<Teacher> teacherByDepartment(Department department, Student_Status status);

    Teacher findTeacherByNames(String first, String middle, String last);

    void updateTeacher(Student_Status status, String last, String first, String cont, String middle,
                       Department department, Gender gender ,String photo , Long id);

    List<Teacher> possibleClassTeachers(Department department);
}
