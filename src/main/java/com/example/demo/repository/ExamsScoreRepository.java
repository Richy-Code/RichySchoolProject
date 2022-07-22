package com.example.demo.repository;

import com.example.demo.entities.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamsScoreRepository extends CrudRepository<ExamsScore, MarksId> {
    @Query("SELECT  ES FROM ExamsScore ES WHERE ES.exams_score_id.class_id = :class_id AND " +
            "ES.exams_score_id.subject_id = :subject_id AND ES.exams_score_id.term_id = :term_id")

    List<ExamsScore> findExamsScores(@Param("class_id")Classes classes, @Param("subject_id")
                                     Subjects subjects, @Param("term_id")Term term);

    @Query("SELECT ES FROM ExamsScore ES WHERE ES.exams_score_id.class_id = :classes " +
            "AND ES.exams_score_id.term_id = :term")
    List<ExamsScore> findExamsScoreByClassAndTerm(@Param("classes") Classes classes,@Param("term")
                                                  Term term);

    @Query("SELECT es FROM ExamsScore es WHERE es.exams_score_id.student_id = :student_Id " +
            "AND es.exams_score_id.class_id.parentClass.class_department = :department")
    List<ExamsScore> examsScoreByStudentIdFromExams(@Param("student_Id") Student student_id,
                                                    @Param("department") Department department);

    @Query("SELECT es FROM Record_Exams es WHERE es.exams_score_id.student_id = :student_Id " +
            "AND es.exams_score_id.class_id.parentClass.class_department = :department")
    List<Record_Exams> examsScoreByStudentIdFromRecordExams(@Param("student_Id") Student student_id,
                                                            @Param("department") Department department);

    @Query("SELECT es FROM ExamsScore  es WHERE es.exams_score_id.student_id = :student AND " +
            "es.exams_score_id.class_id = :class AND es.exams_score_id.term_id = :term")
    List<ExamsScore> examsScoreByStudentTermClass(@Param("student") Student student,@Param("class")
                                                  Classes classes,@Param("term") Term term);

    @Query("SELECT es FROM ExamsScore  es WHERE es.exams_score_id.student_id = :student AND " +
            "es.exams_score_id.term_id = :term AND es.exams_score_id.class_id.parentClass.class_department = :department")
    List<ExamsScore> examsScoreByStudentIdAndTerm(@Param("student")Student student,@Param("term")Term term,
                                                  @Param("department")Department department);

   @Query(value = "TRUNCATE TABLE examsscore",nativeQuery = true)
   void truncateExamsScore();

    @Query("SELECT es FROM ExamsScore  es WHERE es.exams_score_id.student_id = :student AND " +
            "es.exams_score_id.subject_id = :subject")
    List<ExamsScore> examsScoreByStudentIdAndSubject(@Param("student")Student student
            ,@Param("subject")Subjects subjects);

    @Query("SELECT es FROM Record_Exams es WHERE es.exams_score_id.student_id = :student_Id " +
            "AND es.exams_score_id.subject_id = :subject")
    List<Record_Exams> examsScoreByStudentIdAndSubjectFromRecordExams
            (@Param("student_Id") Student student_id,@Param("subject")Subjects subjects);
}
