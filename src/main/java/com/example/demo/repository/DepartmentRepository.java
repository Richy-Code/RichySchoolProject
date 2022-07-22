package com.example.demo.repository;

import com.example.demo.entities.Department;
import com.example.demo.entities.DepartmentHead;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<Department, Long> {
   List<Department> findByHead(DepartmentHead head);

}