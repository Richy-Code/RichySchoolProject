package com.example.demo.repository;

import com.example.demo.entities.Guardian;
import com.example.demo.entities.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GuardianRepository extends CrudRepository<Guardian,Long> {
    @Query("SELECT g FROM Guardian g WHERE g.student = :student")
    Guardian findByStudent(@Param("student")Student student);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Guardian g SET g.student = :student WHERE g.guardian_id = :guardian")
    void updateGuardian(@Param("student")Student student,@Param("guardian") Long guardianId);
}
