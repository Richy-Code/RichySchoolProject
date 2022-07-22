package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repository.ReportDetailRepository;
import com.example.demo.services.service_interface.ReportDetailInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportDetailInterfaceImp implements ReportDetailInterface {
    final ReportDetailRepository repository;

    public ReportDetailInterfaceImp(ReportDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReportDetails> findReportDetailByClassAndTerm(Classes classes, Term term) {
        return repository.findReportDetailsByClassAndTerm(classes,term);
    }

    @Override
    public void saveDetails(ReportDetails details) {
        repository.save(details);
    }

    @Override
    public List<ReportDetails> detailsByStudentId(Student student_id) {
        return repository.findDetailsByStudentId(student_id);
    }

    @Override
    public ReportDetails findDetailsById(ReportDetailsId detailsId) {
        return repository.findById(detailsId).orElse(new ReportDetails());
    }

    @Override
    public void updateHeadRemarks(String remarks, ReportDetailsId detailsId) {
        repository.updateHeadRemark(remarks,detailsId);
    }
}
