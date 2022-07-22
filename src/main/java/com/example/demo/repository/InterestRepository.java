package com.example.demo.repository;

import com.example.demo.entities.Interest;
import org.springframework.data.repository.CrudRepository;

public interface InterestRepository extends CrudRepository<Interest, Long> {
    Interest findByConduct(String conduct);
}
