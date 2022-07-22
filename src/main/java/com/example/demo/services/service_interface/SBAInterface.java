package com.example.demo.services.service_interface;

import com.example.demo.entities.*;

import java.util.List;

public interface SBAInterface {
    List<SBA> sbaList(Classes classes, Subjects subjects, Term term);
    void saveAllSbaList(List<SBA> sbaList);

    List<SBA> findSbaByClassAndTerm(Classes classes, Term term);

    List<SBA> sbaByStudentIdFromSBA(Student student_id,Department department);

    List<SBA> sbaByStudentIdFromRecordSBA(Student student_id,Department department);

    List<SBA> sbaByStudentClassTerm(Student student,Classes classes,Term term);

    List<SBA>findAllByConfig(SBAConfig config);

    List<SBA> sbaByStudentIdAndTerm(Student student,Term term,Department department);

    List<SBA> findAllSba();

    void truncateSba();
}
