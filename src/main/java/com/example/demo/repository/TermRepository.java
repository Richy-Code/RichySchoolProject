package com.example.demo.repository;

import com.example.demo.entities.Term;
import org.springframework.data.repository.CrudRepository;

public interface TermRepository extends CrudRepository<Term,Long> {
}
