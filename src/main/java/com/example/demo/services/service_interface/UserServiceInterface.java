package com.example.demo.services.service_interface;

import com.example.demo.entities.Users;

import java.util.List;

public interface UserServiceInterface {
    void saveUser(Users users);
    Users getUser(String user_name);
    void saveAllUsers(List<Users> usersList);
    List<Users> findAllUsers();
    Users findUserById(Long id);
}
