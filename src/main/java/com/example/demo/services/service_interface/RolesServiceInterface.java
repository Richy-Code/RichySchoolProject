package com.example.demo.services.service_interface;

import com.example.demo.entities.Roles;

import java.util.List;

public interface RolesServiceInterface {
    void saveRole(Roles roles);
    void saveListOfRoles(List<Roles> rolesList);
    Roles findRolesById(Long id);

    List<Roles> rolesList();
}
