package com.example.demo.services;

import com.example.demo.entities.Guardian;
import com.example.demo.entities.Student;
import com.example.demo.repository.GuardianRepository;
import com.example.demo.services.service_interface.GuardianInterface;
import org.springframework.stereotype.Service;

@Service
public class GuardianInterfaceImp implements GuardianInterface {

    private final GuardianRepository repository;

    public GuardianInterfaceImp(GuardianRepository repository) {
        this.repository = repository;
    }

    @Override
    public Guardian findByStudent(Student student) {
        return repository.findByStudent(student);
    }

    @Override
    public void saveGuardian(Guardian guardian) {
        repository.save(guardian);
    }

    @Override
    public void updateGuardian(Student student, Long guardianId) {
        repository.updateGuardian(student,guardianId);
    }


    @Override
    public void deleteGuardian(Guardian guardian) {
        repository.delete(guardian);
    }
}
