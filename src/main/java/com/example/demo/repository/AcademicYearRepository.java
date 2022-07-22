package com.example.demo.repository;

import com.example.demo.entities.Academic_Year;
import com.example.demo.enum_entities.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AcademicYearRepository extends CrudRepository<Academic_Year, Long> {
    @Query("SELECT AY FROM Academic_Year  AY WHERE AY.status = :status")
    Academic_Year findAcademicYearBYMaxId(@Param("status")Status status);

    @Query("SELECT ay FROM Academic_Year ay WHERE ay.status = com.example.demo.enum_entities.Status.PASSED")
    List<Academic_Year> passAcademicYear();

    @Query("SELECT yrs FROM Academic_Year yrs WHERE yrs.first_year = :first AND yrs.last_year = :last")
    Academic_Year findByFirst_yearAndLast_year(@Param("first")String firstYear,@Param("last")String lastYear);
}
