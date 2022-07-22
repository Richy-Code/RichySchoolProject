package com.example.demo.services;

import com.example.demo.entities.Term;
import com.example.demo.repository.TermRepository;
import com.example.demo.services.service_interface.TermInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TermInterfaceImp implements TermInterface {

    final TermRepository termRepository;

    public TermInterfaceImp(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    @Override
    public void saveTerm(Term term) {
        termRepository.save(term);
    }

    @Override
    public void saveAllTerm(List<Term> termList) {
        termRepository.saveAll(termList);
    }

    @Override
    public List<Term> findAllTerm() {
        return (List<Term>) termRepository.findAll();
    }

    @Override
    public Term findTermById(Long id) {
        Optional<Term> term = termRepository.findById(id);
        return term.orElse(null);
    }
}
