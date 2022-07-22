package com.example.demo.services.service_interface;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Department;
import com.example.demo.entities.SBAConfig;
import com.example.demo.enum_entities.Status;

import java.util.List;

public interface SbaConfigInterface {
    SBAConfig findSBAConfig(Department department, Academic_Year year, Status status);
    void saveSbaConfig(SBAConfig sbaConfig);

    SBAConfig findSBAConfigByDepartmentAndStatus(Department department, Status status);
    SBAConfig findSBAConfigByDepartmentAndYear(Department department, Academic_Year year);

    SBAConfig findConfigById(Long configId);

    List<SBAConfig> findAllConfig();

    void updateSbaConfig(Status status,Long configId);
}
