package com.example.demo.services;

import com.example.demo.entities.DepartmentHead;
import com.example.demo.entities.Users;
import com.example.demo.repository.DepartmentHeadRepository;
import com.example.demo.services.service_interface.DepartmentHeadInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentHeadImp implements DepartmentHeadInterface {

    final DepartmentHeadRepository repository;

    public DepartmentHeadImp(DepartmentHeadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveDepartmentHead(List<DepartmentHead> head) {
        repository.saveAll(head);
    }

    @Override
    public DepartmentHead findDepartmentHead(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public DepartmentHead findHeadByUser(Users users) {
        return repository.findByUser(users);
    }
}
