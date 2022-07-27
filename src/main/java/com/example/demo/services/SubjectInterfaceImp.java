package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.entities.Subjects;
import com.example.demo.entities.Teacher;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.enum_entities.SubjectOptions;
import com.example.demo.repository.SubjectRepository;
import com.example.demo.services.service_interface.SubjectInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectInterfaceImp implements SubjectInterface {

    final SubjectRepository subjectRepository;

    public SubjectInterfaceImp(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void saveSubject(Subjects subjects) {
        subjectRepository.save(subjects);
    }

    @Override
    public void saveAllSubject(List<Subjects> subjects) {
        subjectRepository.saveAll(subjects);
    }

    @Override
    public Subjects findSubjectById(Long id) {
        Optional<Subjects> subjects = subjectRepository.findById(id);
        return subjects.orElse(null);
    }

    @Override
    public List<Subjects> subjectsList() {
        return (List<Subjects>) subjectRepository.findAll();
    }

    @Override
    public List<Subjects> findSubjectByTeacher(Teacher teacher) {
        return subjectRepository.teacherSubjects(teacher);
    }


    @Override
    public List<Subjects> subjectByDepartment(Department department, Student_Status status) {
        return subjectRepository.subjectsByDepartment(department, status);
    }

    @Override
    public List<String> teacherSubjectName(Teacher teacher) {
        return subjectRepository.teacherSubjectName(teacher);
    }

    @Override
    public List<Subjects> subjectByOptionAndDepartment(SubjectOptions options, Department department,
                                                       Student_Status status) {
        return subjectRepository.findByOptions(options,department,status);
    }
}
