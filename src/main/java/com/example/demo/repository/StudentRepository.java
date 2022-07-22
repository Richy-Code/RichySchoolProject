package com.example.demo.repository;

import com.example.demo.entities.Classes;
import com.example.demo.entities.Student;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, String> {
    @Query(value = "SELECT s FROM Student s WHERE s.student_status = :status AND s.classes = :class")
    List<Student> listStudentByClass(@Param("status")Student_Status status, @Param("class") Classes classes);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.classes = :class")
    int studentNumberByClass(@Param("class")Classes classes);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Student s SET s.gender = :gender,s.first_name = :first,s.last_name = :last," +
            "s.middle_name = :mid, s.photo_name = :photo WHERE s.student_id = :student")
    void updateStudent(@Param("gender")Gender gender,@Param("first")String first,@Param("last")
                       String last,@Param("mid")String mid,@Param("photo")String photo,
                       @Param("student")String student);
}
