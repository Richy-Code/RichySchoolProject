package com.example.demo.services.service_interface;

import com.example.demo.entities.PlainPassword;
import com.example.demo.entities.Teacher;
import com.example.demo.entities.Users;

public interface PlainPasswordInterface {
    void savePassword(PlainPassword password);
    PlainPassword findPassword(Users user);
}
