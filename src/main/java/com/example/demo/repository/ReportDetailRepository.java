package com.example.demo.repository;

import com.example.demo.entities.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReportDetailRepository extends CrudRepository<ReportDetails, ReportDetailsId> {
    @Query("SELECT RD FROM ReportDetails RD WHERE RD.reportDetailsId.class_id = " +
            ":classes AND RD.reportDetailsId.term_id = :term")
    List<ReportDetails>findReportDetailsByClassAndTerm(@Param("classes")Classes classes, @Param("term")
                                                       Term term);
    @Query("SELECT rd FROM ReportDetails rd WHERE rd.reportDetailsId.student_id = :student_id")
    List<ReportDetails>findDetailsByStudentId(@Param("student_id")Student student_id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE ReportDetails rd SET rd.head_remarks = :remark WHERE rd.reportDetailsId = :id")
    void updateHeadRemark(@Param("remark")String remark,@Param("id")ReportDetailsId detailsId);


}
