package com.example.demo.services.service_interface;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Classes;
import com.example.demo.entities.CommonReportDetails;
import com.example.demo.entities.Term;


public interface CommonDetailsInterface {
    void saveCommonDetails(CommonReportDetails commonReportDetails);

    CommonReportDetails findDetailsByClass_Term_Year(Academic_Year year, Classes classes, Term term);

    void updateCommonReportDetails(int total, Long id);
}
