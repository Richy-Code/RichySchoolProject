package com.example.demo.services.service_interface;

import com.example.demo.entities.Remarks;

import java.util.List;

public interface RemarksInterface {
    void saveRemarks(Remarks remarks);
    List<Remarks> allRemarks();

    void saveAllRemarks(List<Remarks> remarksList);

    Remarks findByRemark(String remark);
}
