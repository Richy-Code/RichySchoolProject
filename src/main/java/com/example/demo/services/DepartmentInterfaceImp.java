package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.entities.DepartmentHead;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.services.service_interface.DepartmentInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentInterfaceImp implements DepartmentInterface {
    final DepartmentRepository departmentRepository;

    public DepartmentInterfaceImp(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public void saveAllDepartment(List<Department> departmentList) {
        departmentRepository.saveAll(departmentList);
    }

    @Override
    public List<Department> findAllDepartment() {
        return (List<Department>) departmentRepository.findAll();
    }

    @Override
    public Department findByDepartmentId(Long id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.orElse(null);
    }

    @Override
    public List<Department> findDepartmentByHead(DepartmentHead head) {
        return departmentRepository.findByHead(head);
    }
}
