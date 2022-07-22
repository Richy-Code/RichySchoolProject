package com.example.demo.services.service_interface;

import com.example.demo.entities.Term;

import java.util.List;

public interface TermInterface {
    void saveTerm(Term term);
    void saveAllTerm(List<Term> termList);
    List<Term>findAllTerm();

    Term findTermById(Long id);
}
