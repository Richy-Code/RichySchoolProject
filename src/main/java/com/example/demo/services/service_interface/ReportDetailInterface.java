package com.example.demo.services.service_interface;

import com.example.demo.entities.*;

import java.util.List;

public interface ReportDetailInterface {
    List<ReportDetails> findReportDetailByClassAndTerm(Classes classes, Term term);
    void saveDetails(ReportDetails details);

    List<ReportDetails> detailsByStudentId(Student student_id);

    ReportDetails findDetailsById(ReportDetailsId detailsId);

    void updateHeadRemarks(String remarks,ReportDetailsId detailsId);
}
