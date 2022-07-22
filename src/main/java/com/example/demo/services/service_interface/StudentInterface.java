package com.example.demo.services.service_interface;

import com.example.demo.entities.Classes;
import com.example.demo.entities.Student;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;

import java.util.List;

public interface StudentInterface {
    void saveStudent(Student student);
    void saveAllStudents(List<Student> students);
    Student findStudentById(String student_id);

    List<Student> studentsByClass(Student_Status status,Classes classes);

    int studentNumberByClass(Classes classes);

    void updateStudent(Gender gender,String first,String last,String mid,
                       String photo,String student);
}
