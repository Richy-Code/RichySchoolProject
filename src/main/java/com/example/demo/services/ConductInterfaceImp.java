package com.example.demo.services;

import com.example.demo.entities.Conduct;
import com.example.demo.repository.ConductRepository;
import com.example.demo.services.service_interface.ConductInterface;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ConductInterfaceImp implements ConductInterface {

    final ConductRepository repository;

    public ConductInterfaceImp(ConductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveConduct(Conduct conduct) {
        repository.save(conduct);
    }

    @Override
    public List<Conduct> findAllConducts() {
        return (List<Conduct>) repository.findAll();
    }

    @Override
    public void saveAllConduct(List<Conduct> conductList) {
        repository.saveAll(conductList);
    }

    @Override
    public Conduct findByConduct(String conduct) {
        return repository.findByConduct(conduct);
    }
}
