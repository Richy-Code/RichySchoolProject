package com.example.demo.repository;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Classes;
import com.example.demo.entities.CommonReportDetails;
import com.example.demo.entities.Term;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommonDetailsRepository extends CrudRepository<CommonReportDetails,Long> {

    @Query("SELECT cd FROM CommonReportDetails cd WHERE cd.academic_year = :year AND cd.classes = :cls AND cd.term = :term")
    CommonReportDetails findCommonByYearAndClasses(@Param("year")Academic_Year year,
                                                   @Param("cls") Classes classes, @Param("term")Term term);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE  CommonReportDetails cd set cd.total_attendance = :attend WHERE cd.common_Details_id = :id")
    void updateCommonDetails(@Param("attend") int attend,@Param("id")Long id);

}
