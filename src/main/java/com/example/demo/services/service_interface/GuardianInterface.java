package com.example.demo.services.service_interface;

import com.example.demo.entities.Guardian;
import com.example.demo.entities.Student;

public interface GuardianInterface {
    Guardian findByStudent(Student student);
    void saveGuardian(Guardian guardian);
   void updateGuardian(Student student, Long guardianId);

   void deleteGuardian(Guardian guardian);
}
