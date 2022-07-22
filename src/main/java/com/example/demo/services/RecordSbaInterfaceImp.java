package com.example.demo.services;

import com.example.demo.entities.Record_SBA;
import com.example.demo.repository.SbaRecordRepository;
import com.example.demo.services.service_interface.SbaRecordInterface;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RecordSbaInterfaceImp implements SbaRecordInterface {

    private final SbaRecordRepository repository;

    public RecordSbaInterfaceImp(SbaRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<Record_SBA> recordSbas) {
        repository.saveAll(recordSbas);
    }
}
