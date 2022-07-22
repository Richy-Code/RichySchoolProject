package com.example.demo.services;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Department;
import com.example.demo.entities.SBAConfig;
import com.example.demo.entities.Term;
import com.example.demo.enum_entities.Status;
import com.example.demo.repository.SbaConfigRepository;
import com.example.demo.services.service_interface.SbaConfigInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SbaConfigInterfaceImp implements SbaConfigInterface {

    private SbaConfigRepository sbaConfigRepository;

    public SbaConfigInterfaceImp(SbaConfigRepository sbaConfigRepository) {
        this.sbaConfigRepository = sbaConfigRepository;
    }

    @Override
    public SBAConfig findSBAConfig(Department department, Academic_Year year, Status status) {
        return sbaConfigRepository.findSBAByDepartment(department,year,status);
    }

    @Override
    public void saveSbaConfig(SBAConfig sbaConfig) {
        sbaConfigRepository.save(sbaConfig);
    }

    @Override
    public SBAConfig findSBAConfigByDepartmentAndStatus(Department department, Status status) {
        return sbaConfigRepository.findSBAByDepartmentAndStatus(department, status);
    }
    @Override
    public SBAConfig findSBAConfigByDepartmentAndYear(Department department, Academic_Year year) {
        return sbaConfigRepository.findSBAByDepartmentAndYear(department, year);
    }

    @Override
    public SBAConfig findConfigById(Long configId) {
        return sbaConfigRepository.findById(configId).orElse(new SBAConfig());
    }

    @Override
    public List<SBAConfig> findAllConfig() {
        return (List<SBAConfig>) sbaConfigRepository.findAll();
    }

    @Override
    public void updateSbaConfig(Status status, Long configId) {
        sbaConfigRepository.updateSbaConfig(status,configId);
    }
}
