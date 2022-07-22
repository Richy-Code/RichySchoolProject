package com.example.demo.services.service_interface;

import com.example.demo.entities.Academic_Year;
import com.example.demo.entities.Term;
import com.example.demo.entities.Term_Info;

import java.util.List;

public interface TermInfoInterface {
    void saveTermInfo(Term_Info term_info);

    List<Term_Info> findTermInfoByAcademicYear(Academic_Year year);

    void saveAllTermInfo(List<Term_Info> term_infos);

    Term_Info termInfoByInfo(Term_Info info);
}
