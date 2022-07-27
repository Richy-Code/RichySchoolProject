package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.entities.OptionalSubject;
import com.example.demo.entities.Subjects;
import com.example.demo.repository.OptionalSubjectRepository;
import com.example.demo.services.service_interface.OptionalSubjectInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OptionalSubjectImp implements OptionalSubjectInterface {

    private  final OptionalSubjectRepository repository;

    public OptionalSubjectImp(OptionalSubjectRepository repository) {
        this.repository = repository;
    }

    public void saveOptionalSubject(OptionalSubject subject) {
        repository.save(subject);
    }

    @Override
    public List<OptionalSubject> findByDepartment(Department department) {
        return repository.findByDepartment_subject(department);
    }

    @Override
    public List<Subjects> optSubjectList(List<Long> subjectIds) {
        return repository.optSubjectList(subjectIds);
    }

    @Override
    public OptionalSubject findOptSubjectById(Long subjectId) {
        return repository.findById(subjectId).orElse(new OptionalSubject());
    }

    @Override
    public OptionalSubject findMainOptSubject(Long subject) {
        return repository.findMainOptSubject(subject);
    }

    @Override
    public void deleteOptionalSubjects(OptionalSubject subject) {
        repository.delete(subject);
    }
}
