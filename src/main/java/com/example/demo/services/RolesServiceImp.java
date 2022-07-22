package com.example.demo.services;

import com.example.demo.entities.Roles;
import com.example.demo.repository.RolesRepository;
import com.example.demo.services.service_interface.RolesServiceInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolesServiceImp implements RolesServiceInterface {
    final RolesRepository repository;

    public RolesServiceImp(RolesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveRole(Roles roles) {
        repository.save(roles);
    }

    @Override
    public void saveListOfRoles(List<Roles> rolesList) {
        repository.saveAll(rolesList);
    }

    @Override
    public Roles findRolesById(Long id) {
        Optional<Roles> roles = repository.findById(id);
        return roles.orElse(null);
    }

    @Override
    public List<Roles> rolesList() {
        return repository.findAll();
    }
}
