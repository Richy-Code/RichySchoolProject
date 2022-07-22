package com.example.demo.services.service_interface;

import com.example.demo.entities.Record_Exams;

import java.util.List;

public interface RecordExamsInterface {
    void saveAll(List<Record_Exams> record_examsList);
}
