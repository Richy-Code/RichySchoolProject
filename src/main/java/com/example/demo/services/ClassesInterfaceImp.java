package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repository.ClassesRepository;
import com.example.demo.services.service_interface.ClassesInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesInterfaceImp implements ClassesInterface {
    final ClassesRepository classesRepository;

    public ClassesInterfaceImp(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    @Override
    public void saveClass(Classes classes) {
        classesRepository.save(classes);
    }

    @Override
    public void saveAllClasses(List<Classes> classes) {
        classesRepository.saveAll(classes);
    }

    @Override
    public Classes findClassById(String id) {
        Optional<Classes> classes = classesRepository.findById(id);
        return classes.orElse(null);
    }
    @Override
    public List<Classes> classList() {
        return (List<Classes>) classesRepository.findAll();
    }

    @Override
    public List<Classes> findClassByDepartment(Department department) {
        return classesRepository.findClassByDepartment(department);
    }

    @Override
    public List<String> findClassByTeacher(Long teacherId) {
        return classesRepository.findClassesByTeacher(teacherId);
    }

    @Override
    public List<Classes> listClassByDepartmentHead(DepartmentHead head) {
        return classesRepository.listClassesByDepartmentHead(head);
    }

    @Override
    public List<Classes> classByParentClass(ParentClass parentClass) {
        return classesRepository.listClassesByParentClass(parentClass);
    }

    @Override
    public void deleteClass(Classes classes) {
        classesRepository.delete(classes);
    }

    @Override
    public void updateSubClassName(String name, String classId) {
        classesRepository.updateSubClassName(name,classId);
    }

}
