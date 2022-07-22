package com.example.demo.services;

import com.example.demo.entities.Remarks;
import com.example.demo.repository.RemarksRepository;
import com.example.demo.services.service_interface.RemarksInterface;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RemarksInterfaceImp implements RemarksInterface {

    final RemarksRepository repository;

    public RemarksInterfaceImp(RemarksRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveRemarks(Remarks remarks) {
        repository.save(remarks);
    }

    @Override
    public List<Remarks> allRemarks() {
        return (List<Remarks>) repository.findAll();
    }

    @Override
    public void saveAllRemarks(List<Remarks> remarksList) {
        repository.saveAll(remarksList);
    }

    @Override
    public Remarks findByRemark(String remark) {
        return repository.findByConduct(remark);
    }
}
