package com.example.demo.services.service_interface;

import com.example.demo.entities.Record_SBA;

import java.util.List;

public interface SbaRecordInterface {
    void saveAll(List<Record_SBA> recordSbas);
}
