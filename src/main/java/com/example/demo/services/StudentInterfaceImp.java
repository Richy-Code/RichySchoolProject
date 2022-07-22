package com.example.demo.services;

import com.example.demo.entities.Classes;
import com.example.demo.entities.Student;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.repository.StudentRepository;
import com.example.demo.services.service_interface.StudentInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentInterfaceImp implements StudentInterface {

    final StudentRepository studentRepository;

    public StudentInterfaceImp(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Override
    public void saveAllStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public Student findStudentById(String student_id) {
        Optional<Student> studentOptional = studentRepository.findById(student_id);
        return studentOptional.orElse(new Student());
    }

    @Override
    public List<Student> studentsByClass( Student_Status status,Classes classes) {
        return studentRepository.listStudentByClass(status,classes);
    }

    @Override
    public int studentNumberByClass(Classes classes) {
        return studentRepository.studentNumberByClass(classes);
    }

    @Override
    public void updateStudent(Gender gender, String first, String last,
                              String mid, String photo, String student) {
        studentRepository.updateStudent(gender,first,last,mid,photo,student);
    }
}
