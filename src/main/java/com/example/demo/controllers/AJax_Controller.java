package com.example.demo.controllers;


import com.example.demo.binding_entities.AjaxTeacher;
import com.example.demo.entities.Classes;
import com.example.demo.entities.Department;
import com.example.demo.entities.Teacher;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.services.service_interface.ClassesInterface;
import com.example.demo.services.service_interface.DepartmentInterface;
import com.example.demo.services.service_interface.TeacherInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
public class AJax_Controller {

    private final TeacherInterface teacherInterface;
    private final DepartmentInterface departmentInterface;

    private final ClassesInterface classesInterface;
    public AJax_Controller(TeacherInterface teacherInterface,
                           DepartmentInterface departmentInterface,
                           ClassesInterface classesInterface) {
        this.teacherInterface = teacherInterface;
        this.departmentInterface = departmentInterface;
        this.classesInterface = classesInterface;
    }

    @RequestMapping(
            value = "/department_teachers/{departmentId}",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<AjaxTeacher>> getDepartmentTeachers(@PathVariable("departmentId") String departmentId){
        Department department = departmentInterface.findByDepartmentId(Long.parseLong(departmentId));
        List<Teacher> teachers = teacherInterface.teacherByDepartment(department, Student_Status.ACTIVE);
        List<Classes> classesList = classesInterface.findClassByDepartment(department);
        List<AjaxTeacher> ajaxTeachers = new ArrayList<>();
        for (Classes classes : classesList){
            teachers.remove(classes.getClass_teacher_id());
        }
        teachers.forEach(teacher -> ajaxTeachers.add(new AjaxTeacher(teacher.getTeacher_id(),
                teacher.getStaffFullName())));
        try {
            return new ResponseEntity<>(
                    ajaxTeachers, HttpStatus.OK
            );
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(
            value = "/possible_teacher/{departmentId}",
            method = RequestMethod.GET,
            produces = {MimeTypeUtils.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<List<AjaxTeacher>> getPossibleSubjectTeachers(@PathVariable("departmentId")
                                                                       String departmentId){
        Department department = departmentInterface.findByDepartmentId(Long.parseLong(departmentId));
        List<Teacher> teachers = teacherInterface.teacherByDepartment(department, Student_Status.ACTIVE);
        List<AjaxTeacher> ajaxTeachers = new ArrayList<>();
        teachers.forEach(teacher -> ajaxTeachers.add(new AjaxTeacher(teacher.getTeacher_id(),
                teacher.getStaffFullName())));
        try {
            return new ResponseEntity<>(ajaxTeachers,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
