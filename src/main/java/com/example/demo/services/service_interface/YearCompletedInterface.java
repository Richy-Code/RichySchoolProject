package com.example.demo.services.service_interface;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.StudentCompletedYear;

import java.util.List;

public interface YearCompletedInterface {
    void saveStudentCompletedYear(List<StudentCompletedYear> completedYears);
    List<StudentCompletedYear> studentByCompletedYear(Academic_Year year);

    List<Academic_Year> yearCompleted();
}
