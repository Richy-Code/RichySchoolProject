package com.example.demo.repository;

import com.example.demo.entities.Remarks;
import org.springframework.data.repository.CrudRepository;

public interface RemarksRepository extends CrudRepository<Remarks, Long> {
    Remarks findByConduct(String conduct);
}
