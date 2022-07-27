package com.example.demo.services.service_interface;

import com.example.demo.binding_entities.SummaryReport;
import com.example.demo.entities.*;

import java.util.List;

public interface ExamsScoreInterface {
    List<ExamsScore> examsScores(Classes classes , Subjects subjects, Term term);
    void saveAllExamsScore(List<ExamsScore> examsScores) ;

    List<ExamsScore> findExamsScoreByClassAndTerm(Classes classes, Term term);

    List<ExamsScore> examsScoreByStudentIdFromExams(Student student_id,Department department);

    List<ExamsScore> examsScoreByStudentIdFromRecordExams(Student student_id,Department department);

    List<ExamsScore> examsScoreByStudentClassTerm(Student student,Classes classes, Term term);

    List<ExamsScore> scoreByStudentIdAndTerm(Student student, Term term,Department department);

    List<ExamsScore> findAllScore();

    void truncatexamscore();

    List<ExamsScore> examsScoreByStudentAndSubject(Student student,Subjects subjects);

    List<ExamsScore> examsScoreByStudentAndSubjectFromRecordExams(Student student,Subjects subjects);

    List<SummaryReport> summaryReport(String studentId, Department department);

    List<SummaryReport> summaryReportFromRecordTable(String studentId, Department department);
}
