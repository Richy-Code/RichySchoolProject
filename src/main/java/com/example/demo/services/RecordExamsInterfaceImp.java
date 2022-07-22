package com.example.demo.services;

import com.example.demo.entities.Record_Exams;
import com.example.demo.repository.RecordExamsRepository;
import com.example.demo.services.service_interface.RecordExamsInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordExamsInterfaceImp implements RecordExamsInterface {

    private final RecordExamsRepository repository;

    public RecordExamsInterfaceImp(RecordExamsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveAll(List<Record_Exams> record_examsList) {
        repository.saveAll(record_examsList);
    }
}
