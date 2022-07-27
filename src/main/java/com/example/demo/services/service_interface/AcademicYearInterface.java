package com.example.demo.services.service_interface;

import com.example.demo.entities.Academic_Year;
import com.example.demo.enum_entities.Status;

import java.util.List;

public interface AcademicYearInterface {
    Academic_Year findAcademicYearByMaxId(Status status);
    void saveAcademicYear(Academic_Year year);

    Academic_Year findAcademicYearById(Long id);

    List<Academic_Year> passAcademicYear();

    Academic_Year findByFirstAndLastYear(String firstYear, String lastYear);


}
