package com.example.demo.services;

import com.example.demo.binding_entities.SummaryReport;
import com.example.demo.entities.*;
import com.example.demo.repository.ExamsScoreRepository;
import com.example.demo.services.service_interface.AcademicYearInterface;
import com.example.demo.services.service_interface.ExamsScoreInterface;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamsScoreInterfaceImp implements ExamsScoreInterface {
    private final ExamsScoreRepository repository;
    private final AcademicYearInterface academicYearInterface;
    public ExamsScoreInterfaceImp(ExamsScoreRepository repository, AcademicYearInterface academicYearInterface) {
        this.repository = repository;
        this.academicYearInterface = academicYearInterface;
    }

    @Override
    public List<ExamsScore> examsScores(Classes classes, Subjects subjects, Term term) {
        return repository.findExamsScores(classes,subjects,term);
    }

    @Override
    public void saveAllExamsScore(List<ExamsScore> examsScores) {
        repository.saveAll(examsScores);
    }

    @Override
    public List<ExamsScore> findExamsScoreByClassAndTerm(Classes classes, Term term) {
        return repository.findExamsScoreByClassAndTerm(classes,term);
    }

    @Override
    public List<ExamsScore> examsScoreByStudentIdFromExams(Student student_id, Department department) {
        return repository.examsScoreByStudentIdFromExams(student_id,department);
    }

    @Override
    public List<ExamsScore> examsScoreByStudentIdFromRecordExams(Student student_id,Department department) {
        List<ExamsScore> examsScoreList = new ArrayList<>();
        for (Record_Exams exams : repository.examsScoreByStudentIdFromRecordExams(student_id,department)){
            examsScoreList.add(new ExamsScore(exams.getExams_score_id(),exams.getMarks(),exams.getPosition(),
                    academicYearInterface.findAcademicYearById(exams.getAcademic_yearId())));
        }
        return examsScoreList;
    }

    @Override
    public List<ExamsScore> examsScoreByStudentClassTerm(Student student, Classes classes, Term term) {
        return repository.examsScoreByStudentTermClass(student,classes,term);
    }

    @Override
    public List<ExamsScore> scoreByStudentIdAndTerm(Student student, Term term,Department department) {
        return repository.examsScoreByStudentIdAndTerm(student,term,department);
    }


    @Override
    public List<ExamsScore> findAllScore() {
        return (List<ExamsScore>) repository.findAll();
    }


    @Override
    public void truncatexamscore() {
        repository.truncateExamsScore();
    }

    @Override
    public List<ExamsScore> examsScoreByStudentAndSubject(Student student, Subjects subjects) {
        return repository.examsScoreByStudentIdAndSubject(student,subjects);
    }

    @Override
    public List<ExamsScore> examsScoreByStudentAndSubjectFromRecordExams(Student student, Subjects subjects) {
        List<ExamsScore> examsScoreList = new ArrayList<>();
        for (Record_Exams exams : repository.examsScoreByStudentIdAndSubjectFromRecordExams(student,subjects)){
            examsScoreList.add(new ExamsScore(exams.getExams_score_id(),exams.getMarks(),exams.getPosition(),
                    academicYearInterface.findAcademicYearById(exams.getAcademic_yearId())));
        }
        return examsScoreList;
    }

    @Override
    public List<SummaryReport> summaryReport(String studentId, Department department) {
        List<SummaryReport> summaryReports = new ArrayList<>();
        String query = """
                SELECT subject_name, class_name,term_name,marks from classes,subjects,terms,exams_mark
                           where subjects.subject_id = exams_mark.subject_id  and classes.class_id = exams_mark.class_id
                           and terms.term_id = exams_mark.term_id and subjects.department_subject = ? AND 
                          exams_mark.student_id = ?
                """;
        try{
            PreparedStatement ps = connection().prepareStatement(query);
            ps.setString(2,studentId);
            ps.setLong(1,department.getDepartmentID());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                summaryReports.add(new SummaryReport(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getDouble(4)));
            }
        }catch (SQLException es){
            es.printStackTrace();
        }
        return summaryReports;
    }


    @Override
    public List<SummaryReport> summaryReportFromRecordTable(String studentId, Department department) {
        List<SummaryReport> summaryReports = new ArrayList<>();
        String query = """
                SELECT subject_name, class_name,term_name,marks from classes,subjects,terms,exams_mark_record
                           where subjects.subject_id = exams_mark_record.subject_id  and classes.class_id = exams_mark_record.class_id
                           and terms.term_id = exams_mark_record.term_id and subjects.department_subject = ? AND 
                          exams_mark_record.student_id = ?
                """;
        try{
            PreparedStatement ps = connection().prepareStatement(query);
            ps.setString(2,studentId);
            ps.setLong(1,department.getDepartmentID());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()){
                summaryReports.add(new SummaryReport(resultSet.getString(1),resultSet.getString(2),
                        resultSet.getString(3),resultSet.getDouble(4)));
            }
        }catch (SQLException es){
            es.printStackTrace();
        }
        return summaryReports;
    }

    private Connection connection(){
        final String URL = "jdbc:mysql://localhost:3306/school_project_db?useSSL=false";
        final String USERNAME = "root";
        final String PASSWORD = "RichyRich";
        Connection conn = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        }catch (ClassNotFoundException | SQLException e){
            System.out.println(e.getMessage());

        }
        return conn;
    }
}
