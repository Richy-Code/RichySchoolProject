package com.example.demo.services.service_interface;

import com.example.demo.entities.Department;
import com.example.demo.entities.ParentClass;

import java.util.List;

public interface ParentClassInterface {
    List<ParentClass> findAllParentClass();

    ParentClass findParentClassById(Long parentClassId);

    List<ParentClass> findByDepartment(Department department);

    void saveAllParentClasses(List<ParentClass> parentClassList);

    ParentClass parentClassByName(String name);
}
