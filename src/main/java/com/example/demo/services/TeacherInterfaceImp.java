package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.entities.Teacher;
import com.example.demo.entities.Users;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.services.service_interface.TeacherInterface;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherInterfaceImp implements TeacherInterface {

    final TeacherRepository teacherRepository;

    public TeacherInterfaceImp(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public void saveTeacher(Teacher teacher) {
        teacher.getUsers().setPassword(new
                BCryptPasswordEncoder().encode(teacher.getUsers().getPassword()));
        teacherRepository.save(teacher);
    }

    @Override
    public void saveAllTeachers(List<Teacher> teacherList) {
        teacherRepository.saveAll(teacherList);
    }

    @Override
    public List<Teacher> findAllTeachers() {
        return (List<Teacher>) teacherRepository.findAll();
    }

    @Override
    public Teacher findTeacherByID(Long Id) {
        Optional<Teacher> teacher = teacherRepository.findById(Id);
        return teacher.orElse(null);
    }

    @Override
    public Teacher findTeacherByUsers(Users users) {
        return teacherRepository.findTeacherByUser(users);
    }

    @Override
    public List<Teacher> teacherByDepartment(Department department, Student_Status status) {
        return teacherRepository.teacherByDepartment(department,status);
    }

    @Override
    public Teacher findTeacherByNames(String first, String middle, String last) {
        return teacherRepository.teacherByNames(first,middle,last);
    }

    @Override
    public void updateTeacher(Student_Status status, String last, String first, String cont,
                              String middle, Department department, Gender gender,
                              String photo, Long id) {
        teacherRepository.updateTeacher(status,last,first,cont,middle,department,gender,photo,id);
    }

    @Override
    public List<Teacher> possibleClassTeachers(Department department) {
        return teacherRepository.possibleClassTeachers(department);
    }
}