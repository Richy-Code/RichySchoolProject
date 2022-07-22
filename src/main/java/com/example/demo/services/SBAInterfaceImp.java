package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repository.SBARepository;
import com.example.demo.services.service_interface.AcademicYearInterface;
import com.example.demo.services.service_interface.SBAInterface;
import com.example.demo.services.service_interface.SbaConfigInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SBAInterfaceImp implements SBAInterface {

   private final SBARepository sbaRepository;
   private final AcademicYearInterface academicYearInterface;
   private final SbaConfigInterface configInterface;
    public SBAInterfaceImp(SBARepository sbaRepository, AcademicYearInterface academicYearInterface,
                           SbaConfigInterface configInterface) {
        this.sbaRepository = sbaRepository;
        this.academicYearInterface = academicYearInterface;
        this.configInterface = configInterface;
    }

    @Override
    public List<SBA> sbaList(Classes classes, Subjects subjects, Term term) {
        return sbaRepository.findSBA(classes,subjects,term);
    }

    @Override
    public void saveAllSbaList(List<SBA> sbaList) {
        sbaRepository.saveAll(sbaList);
    }

    @Override
    public List<SBA> findSbaByClassAndTerm(Classes classes, Term term) {
        return sbaRepository.findSBAByClassAndTerm(classes,term);
    }

    @Override
    public List<SBA> sbaByStudentIdFromSBA(Student student_id,Department department) {
        return sbaRepository.sbaByStudentIdFromSba(student_id,department);
    }

    @Override
    public List<SBA> sbaByStudentIdFromRecordSBA(Student student_id,Department department) {
        List<SBA> sbaList = new ArrayList<>();
        for (Record_SBA sba : sbaRepository.sbaByStudentIdFromRecord_Sba(student_id,department)){
            sbaList.add(new SBA(sba.getMarksId(),sba.getMarks(),
                    academicYearInterface.findAcademicYearById(sba.getAcademic_yearId()
                    ), configInterface.findConfigById(sba.getSba_mark_id())));
        }
        return sbaList;
    }

    @Override
    public List<SBA> sbaByStudentClassTerm(Student student, Classes classes, Term term) {
        return sbaRepository.sbaByStudentClassTerm(student,classes,term);
    }


    @Override
    public List<SBA> findAllByConfig(SBAConfig config) {
        return sbaRepository.findAllBySbaConfig(config);
    }


    @Override
    public List<SBA> sbaByStudentIdAndTerm(Student student, Term term,Department department) {
        return sbaRepository.sbaByStudentIdAndTerm(student,term,department);
    }

    @Override
    public List<SBA> findAllSba() {
        return (List<SBA>) sbaRepository.findAll();
    }


    @Override
    public void truncateSba() {
        sbaRepository.truncateSba();
    }
}
