package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.entities.ParentClass;
import com.example.demo.repository.ParentClassRepository;
import com.example.demo.services.service_interface.ParentClassInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParentClassInterfaceImp implements ParentClassInterface {

    private final ParentClassRepository repository;

    public ParentClassInterfaceImp(ParentClassRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ParentClass> findAllParentClass() {
        return (List<ParentClass>) repository.findAll();
    }

    @Override
    public ParentClass findParentClassById(Long parentClassId) {
        return repository.findById(parentClassId).orElse(new ParentClass());
    }

    @Override
    public List<ParentClass> findByDepartment(Department department) {
        return repository.findByClass_department(department);
    }

    @Override
    public void saveAllParentClasses(List<ParentClass> parentClassList) {
        repository.saveAll(parentClassList);
    }

    @Override
    public ParentClass parentClassByName(String name) {
        return repository.parentClassByName(name);
    }
}
