package com.example.demo.services;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Classes;
import com.example.demo.entities.CommonReportDetails;
import com.example.demo.entities.Term;
import com.example.demo.repository.CommonDetailsRepository;
import com.example.demo.services.service_interface.CommonDetailsInterface;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class CommonDetailsInterfaceImp implements CommonDetailsInterface {

    final CommonDetailsRepository  repository;

    public CommonDetailsInterfaceImp(CommonDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveCommonDetails(CommonReportDetails commonReportDetails) {
        repository.save(commonReportDetails);
    }

    @Override
    public CommonReportDetails findDetailsByClass_Term_Year(Academic_Year year, Classes classes, Term term) {
        return repository.findCommonByYearAndClasses(year,classes,term);
    }

    @Override
    public void updateCommonReportDetails(int total, Long id) {
        repository.updateCommonDetails(total,id);
    }

}
