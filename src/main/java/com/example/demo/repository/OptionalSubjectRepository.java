package com.example.demo.repository;

import com.example.demo.entities.Department;
import com.example.demo.entities.OptionalSubject;
import com.example.demo.entities.Subjects;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionalSubjectRepository extends CrudRepository<OptionalSubject, Long> {
    @Query("SELECT opt FROM OptionalSubject opt WHERE opt.department_subject = :dept")
    List<OptionalSubject> findByDepartment_subject(@Param("dept")Department department);

    @Query("SELECT sub FROM Subjects sub, OptionalSubject opt WHERE sub.subject_id IN :subIds")
    List<Subjects> optSubjectList(@Param("subIds") List<Long> subIds);

    @Query(value = "SELECT * FROM optional_subject op, opt_subject opt WHERE opt.subject_id = :subject " +
            "AND op.opt_sub_id = opt.opt_sub_id",nativeQuery = true)
    OptionalSubject findMainOptSubject(@Param("subject")Long subjectId);

}
