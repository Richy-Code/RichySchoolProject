package com.example.demo.services.service_interface;

import com.example.demo.entities.Department;
import com.example.demo.entities.DepartmentHead;

import java.util.List;

public interface DepartmentInterface {
    void saveDepartment(Department department);
    void saveAllDepartment(List<Department> departmentList);
    List<Department> findAllDepartment();

    Department findByDepartmentId(Long id);

    List<Department> findDepartmentByHead(DepartmentHead head);
}
