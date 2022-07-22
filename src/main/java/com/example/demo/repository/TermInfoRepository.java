package com.example.demo.repository;


import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Term_Info;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TermInfoRepository extends CrudRepository<Term_Info, Long> {
    @Query("SELECT t_info FROM Term_Info t_info WHERE t_info.academic_year = :year")
    List<Term_Info> findTermInfoByAcademicYear(@Param("year")Academic_Year year);

    @Query("SELECT info FROM Term_Info info WHERE info = :info")
    Term_Info termInfoByInfo(@Param("info") Term_Info info);
}
