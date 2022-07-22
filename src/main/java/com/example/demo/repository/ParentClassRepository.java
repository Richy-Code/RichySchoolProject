package com.example.demo.repository;

import com.example.demo.entities.Department;
import com.example.demo.entities.ParentClass;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParentClassRepository extends CrudRepository<ParentClass, Long> {
    @Query("SELECT pc FROM ParentClass pc WHERE pc.class_department = :department")
    List<ParentClass> findByClass_department(@Param("department")Department department);

    @Query("SELECT pc FROM ParentClass pc WHERE pc.parentClassName = :name")
    ParentClass parentClassByName(@Param("name") String name);
}
