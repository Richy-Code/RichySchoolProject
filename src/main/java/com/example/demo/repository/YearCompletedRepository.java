package com.example.demo.repository;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.StudentCompletedYear;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YearCompletedRepository extends CrudRepository<StudentCompletedYear,Long> {
    @Query("SELECT stu FROM StudentCompletedYear stu WHERE stu.year = :year")
    List<StudentCompletedYear> studentsByYearCompleted(@Param("year") Academic_Year year);

    @Query("SELECT ac.year FROM StudentCompletedYear ac")
    List<Academic_Year> yearsCompleted();
}
