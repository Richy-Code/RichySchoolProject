package com.example.demo.repository;

import com.example.demo.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SBARepository extends CrudRepository<SBA, MarksId> {

    @Query("SELECT SB FROM SBA SB WHERE SB.marksId.class_id = :class_id AND " +
            "SB.marksId.subject_id = :subject_id AND SB.marksId.term_id = :term_id"
    )
    List<SBA> findSBA(@Param("class_id") Classes classes, @Param("subject_id")
                            Subjects subjects, @Param("term_id") Term term);

    @Query("SELECT SB FROM SBA SB WHERE SB.marksId.class_id = :classes AND " +
            "SB.marksId.term_id = :term")
    List<SBA> findSBAByClassAndTerm(@Param("classes")Classes classes,@Param("term")Term term);

    @Query("SELECT sb FROM Record_SBA sb WHERE sb.marksId.student_id = :student_id AND " +
            "sb.marksId.class_id.parentClass.class_department= :department")
    List<Record_SBA> sbaByStudentIdFromRecord_Sba(@Param("student_id") Student student_id,
                                                  @Param("department")Department department);

    @Query("SELECT sb FROM SBA sb WHERE sb.marksId.student_id = :student_id AND " +
            "sb.marksId.class_id.parentClass.class_department= :department")
    List<SBA> sbaByStudentIdFromSba(@Param("student_id") Student student_id,
                                    @Param("department")Department department);

    @Query("select sb from SBA sb WHERE sb.marksId.student_id = :student AND sb.marksId.class_id = :class AND " +
            "sb.marksId.term_id = :term")
    List<SBA> sbaByStudentClassTerm(@Param("student") Student student,@Param("class") Classes classes,
                                    @Param("term") Term term);
    List<SBA> findAllBySbaConfig(SBAConfig config);

    @Query("SELECT sb FROM SBA sb WHERE sb.marksId.student_id = :student_id AND sb.marksId.term_id = :term AND " +
            "sb.marksId.class_id.parentClass.class_department= :department")
    List<SBA> sbaByStudentIdAndTerm(@Param("student_id")Student student,@Param("term")Term term,
                                    @Param("department")Department department);

    @Query(value = "TRUNCATE TABLE sba",nativeQuery = true)
    void truncateSba();
}
