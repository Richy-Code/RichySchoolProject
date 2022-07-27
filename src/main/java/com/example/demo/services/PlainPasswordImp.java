package com.example.demo.services;

import com.example.demo.entities.PlainPassword;
import com.example.demo.entities.Teacher;
import com.example.demo.entities.Users;
import com.example.demo.repository.PlainPasswordRepository;
import com.example.demo.services.service_interface.PlainPasswordInterface;
import org.springframework.stereotype.Service;

@Service
public class PlainPasswordImp implements PlainPasswordInterface {

    private final PlainPasswordRepository repository;

    public PlainPasswordImp(PlainPasswordRepository repository) {
        this.repository = repository;
    }

    @Override
    public void savePassword(PlainPassword password) {
        repository.save(password);
    }

    @Override
    public PlainPassword findPassword(Users users) {
      return   repository.findPlainPasswordByUsers(users);
    }
}
