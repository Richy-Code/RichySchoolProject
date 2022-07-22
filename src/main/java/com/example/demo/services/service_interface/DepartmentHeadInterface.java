package com.example.demo.services.service_interface;

import com.example.demo.entities.DepartmentHead;
import com.example.demo.entities.Users;

import java.util.List;

public interface DepartmentHeadInterface {
    void saveDepartmentHead(List<DepartmentHead> head);

    DepartmentHead findDepartmentHead(Long id);

    DepartmentHead findHeadByUser(Users users);
}
