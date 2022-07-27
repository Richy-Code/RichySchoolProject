package com.example.demo.repository;

import com.example.demo.entities.PlainPassword;
import com.example.demo.entities.Teacher;
import com.example.demo.entities.Users;
import org.springframework.data.repository.CrudRepository;

public interface PlainPasswordRepository extends CrudRepository<PlainPassword, Long> {

    PlainPassword findPlainPasswordByUsers(Users users);
}
