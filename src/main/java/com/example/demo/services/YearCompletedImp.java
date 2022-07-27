package com.example.demo.services;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.StudentCompletedYear;
import com.example.demo.repository.YearCompletedRepository;
import com.example.demo.services.service_interface.YearCompletedInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YearCompletedImp implements YearCompletedInterface {
    private final YearCompletedRepository repository;

    public YearCompletedImp(YearCompletedRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveStudentCompletedYear(List<StudentCompletedYear> completedYears) {
        repository.saveAll(completedYears);
    }

    @Override
    public List<StudentCompletedYear> studentByCompletedYear(Academic_Year year) {
        return repository.studentsByYearCompleted(year);
    }

    @Override
    public List<Academic_Year> yearCompleted() {
        return repository.yearsCompleted();
    }
}
