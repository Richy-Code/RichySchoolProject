package com.example.demo.services;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Term_Info;
import com.example.demo.repository.TermInfoRepository;
import com.example.demo.services.service_interface.TermInfoInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TermInfoInterfaceImp implements TermInfoInterface {

    private TermInfoRepository termInfoRepository;

    public TermInfoInterfaceImp(TermInfoRepository termInfoRepository) {
        this.termInfoRepository = termInfoRepository;
    }

    @Override
    public void saveTermInfo(Term_Info term_info) {
        termInfoRepository.save(term_info);
    }

    @Override
    public List<Term_Info> findTermInfoByAcademicYear(Academic_Year year) {
        return termInfoRepository.findTermInfoByAcademicYear(year);
    }

    @Override
    public void saveAllTermInfo(List<Term_Info> term_infos) {
        termInfoRepository.saveAll(term_infos);
    }

    @Override
    public Term_Info termInfoByInfo(Term_Info info) {
        return termInfoRepository.termInfoByInfo(info);
    }
}
