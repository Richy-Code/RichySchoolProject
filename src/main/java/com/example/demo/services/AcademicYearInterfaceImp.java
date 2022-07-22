package com.example.demo.services;

import com.example.demo.entities.Academic_Year;
import com.example.demo.enum_entities.Status;
import com.example.demo.repository.AcademicYearRepository;
import com.example.demo.services.service_interface.AcademicYearInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcademicYearInterfaceImp implements AcademicYearInterface {

    private AcademicYearRepository repository;

    public AcademicYearInterfaceImp(AcademicYearRepository repository) {
        this.repository = repository;
    }

    @Override
    public Academic_Year findAcademicYearByMaxId(Status status) {
        return repository.findAcademicYearBYMaxId(status);
    }

    @Override
    public void saveAcademicYear(Academic_Year year) {
        repository.save(year);
    }

    @Override
    public Academic_Year findAcademicYearById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Academic_Year> passAcademicYear() {
        return repository.passAcademicYear();
    }

    @Override
    public Academic_Year findByFirstAndLastYear(String firstYear, String lastYear) {
        return repository.findByFirst_yearAndLast_year(firstYear,lastYear);
    }
}
