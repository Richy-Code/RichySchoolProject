package com.example.demo.repository;

import com.example.demo.entities.Conduct;
import org.springframework.data.repository.CrudRepository;

public interface ConductRepository extends CrudRepository<Conduct,Long> {
    Conduct findByConduct(String conduct);
}
