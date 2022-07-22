package com.example.demo.repository;

import com.example.demo.entities.DepartmentHead;
import com.example.demo.entities.Users;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentHeadRepository extends CrudRepository<DepartmentHead, Long> {
    DepartmentHead findByUser(Users users);
}
