package com.example.demo.services.service_interface;

import com.example.demo.entities.PlainPassword;
import com.example.demo.entities.Teacher;

public interface PlainPasswordInterface {
    void savePassword(PlainPassword password);
    PlainPassword findPassword(Teacher teacher);
}
