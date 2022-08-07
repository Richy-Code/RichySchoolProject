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
import java.util.Set;

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
    public List<Subjects> findSubjectByTeacher(Long teacherId) {
        return subjectRepository.teacherSubjects(teacherId);
    }


    @Override
    public List<Subjects> subjectByDepartment(Department department, Student_Status status) {
        return subjectRepository.subjectsByDepartment(department, status);
    }

    @Override
    public List<String> teacherSubjectName(Long teacherId) {
        return subjectRepository.teacherSubjectName(teacherId);
    }

    @Override
    public List<Subjects> subjectByOptionAndDepartment(SubjectOptions options, Department department,
                                                       Student_Status status) {
        return subjectRepository.findByOptions(options,department,status);
    }

    @Override
    public void updateSubject(Set<Teacher> set, String subjectName, Long subjectId) {
       subjectRepository.updateSubject(set,subjectName,subjectId);
    }
}
