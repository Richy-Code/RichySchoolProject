package com.example.demo.controllers;

import com.example.demo.binding_entities.*;
import com.example.demo.entities.*;
import com.example.demo.enum_entities.Status;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.enum_entities.SubjectOptions;
import com.example.demo.error.AppExceptions;
import com.example.demo.error.NoPassRecordFoundException;
import com.example.demo.methods.Help;
import com.example.demo.services.service_interface.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class Teacher_Controller {
    private final UserServiceInterface userServiceInterface;
    private final SubjectInterface subjectInterface;

    private final StudentInterface studentInterface;
    private final ClassesInterface classesInterface;

    private final ExamsScoreInterface examsScoreInterface;

    private final TermInterface termInterface;

    private final SBAInterface sbaInterface;
    private final SbaConfigInterface sbaConfigInterface;

    private final AcademicYearInterface academicYearInterface;

    private final ReportDetailInterface reportDetailInterface;

    private final TermInfoInterface termInfoInterface;

    private final TeacherInterface teacherInterface;

    private final RemarksInterface remarksInterface;

    private final InterestInterface interestInterface;

    private final ConductInterface conductInterface;
    private final CommonDetailsInterface commonDetailsInterface;

    private final GuardianInterface guardianInterface;
    private final List<Report> reportList = new ArrayList<>();
    private final List<Report> reportFillingList = new ArrayList<>();

    private final List<Report> passReportList = new ArrayList<>();
    public Teacher_Controller(StudentInterface studentInterface,
                              UserServiceInterface userServiceInterface,
                              SubjectInterface subjectInterface,
                              ClassesInterface classesInterface,
                              ExamsScoreInterface examsScoreInterface,
                              TermInterface termInterface,
                              SBAInterface sbaInterface,
                              SbaConfigInterface sbaConfigInterface,
                              AcademicYearInterface academicYearInterface,
                              ReportDetailInterface reportDetailInterface,
                              TermInfoInterface termInfoInterface,
                              TeacherInterface teacherInterface,
                              RemarksInterface remarksInterface,
                              InterestInterface interestInterface,
                              ConductInterface conductInterface,
                              CommonDetailsInterface commonDetailsInterface,
                              GuardianInterface guardianInterface) {
        this.studentInterface = studentInterface;
        this.userServiceInterface = userServiceInterface;
        this.subjectInterface = subjectInterface;
        this.classesInterface = classesInterface;
        this.examsScoreInterface = examsScoreInterface;
        this.termInterface = termInterface;
        this.sbaInterface = sbaInterface;
        this.sbaConfigInterface = sbaConfigInterface;
        this.academicYearInterface = academicYearInterface;
        this.reportDetailInterface = reportDetailInterface;
        this.termInfoInterface = termInfoInterface;
        this.teacherInterface = teacherInterface;
        this.remarksInterface = remarksInterface;
        this.interestInterface = interestInterface;
        this.conductInterface = conductInterface;
        this.commonDetailsInterface = commonDetailsInterface;
        this.guardianInterface = guardianInterface;
    }

    @RequestMapping(value = "/teacher_exams_entry_form")
    public String hello(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Subjects> teacherSubjects = subjectInterface.findSubjectByTeacher(teacher);
        List<Classes> classes = classesInterface.classList();
        List<Classes> teacherClasses = new ArrayList<>();
        String class_teacher_id = "";
        for (Classes classes1 : classes){
            Optional<Teacher> optional = Optional.ofNullable(classes1.getClass_teacher_id());
            if (optional.isEmpty())
                class_teacher_id = "";
            else{
                if (classes1.getClass_teacher_id().equals(teacher)) {
                    class_teacher_id = classes1.getClass_id();
                }
            }
        }
        for (Classes classes1 : classes){
            for (Teacher teacher1 : classes1.getClass_teachers()){
                if (teacher1.equals(teacher)){
                    teacherClasses.add(classes1);
                }
            }
        }
        model.addAttribute("classes",teacherClasses);
        model.addAttribute("subjects", teacherSubjects);
        model.addAttribute("cls_id",class_teacher_id);
        if(teacherSubjects.size() ==1 && class_teacher_id.equals(""))
            return "exam_home_page_one_sub";
        else if(teacherSubjects.size() == 1)
            return "exams_home_page_class_teacher_one";
        else if(teacherSubjects.size() > 1 && class_teacher_id.equals(""))
            return "exams_home_two";
        else
            return "exams_entry_home_page_class_teacher_two";
    }

    @RequestMapping(value = "/exams_page")
    public String examsEntry(HttpServletRequest servletRequest,Model model){
        ExamsEntry examsEntry;
        Exams_EntrySet exams_entrySet = new Exams_EntrySet();
        List<ExamsEntry> examsEntryList = new ArrayList<>();
        Long term_id = Long.parseLong(servletRequest.getParameter("trm"));
        Long subject_id = Long.parseLong(servletRequest.getParameter("sub"));
        String class_id = servletRequest.getParameter("cls");
        Classes classes = classesInterface.findClassById(class_id);
        Subjects subjects = subjectInterface.findSubjectById(subject_id);
        Term term = termInterface.findTermById(term_id);
        List<Student> studentList;
        List<ExamsScore> examsScores = examsScoreInterface.examsScores(classes,subjects,term);
        List<SBA> sbaList = sbaInterface.sbaList(classes,subjects,term);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        if(sbaConfig == null){
            sbaConfig = new SBAConfig();
        }
        if (subjects.getOptions().equals(SubjectOptions.OPTIONAL)) {
            studentList = new ArrayList<>((Collection) subjects.getStudentSet().stream().filter(
                    student -> student.getStudent_status().equals(Student_Status.ACTIVE) &&
                            student.getClasses().equals(classes)
            ));
        }else {
            studentList  = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        }
        List<ExamsScore> previous = null;
        if (examsScores.isEmpty() && sbaList.isEmpty()){
            for (Student student : studentList){
                examsEntry = new ExamsEntry();
                examsEntry.setStudentId(student.getStudent_id());
                examsEntry.setStudentFull_name(student.getStudentFullName());
                examsEntryList.add(examsEntry);
            }
        } else if (!examsScores.isEmpty() && sbaList.isEmpty()) {
            for (Student student : studentList){
                for (ExamsScore score : examsScores){
                    if (student.equals(score.getExams_score_id().getStudent_id())){
                        examsEntry = new ExamsEntry();
                        examsEntry.setStudentId(student.getStudent_id());
                        examsEntry.setStudentFull_name(student.getStudentFullName());
                        examsEntry.setExamsScore(score.getMarks());
                        examsEntry.setGrade(Help.grade(examsEntry.getExamsScore() *
                                (100-sbaConfig.getClasswork_scale())/100));
                        examsEntry.setTotal_exam_marks(examsEntry.getExamsScore() *
                                (100-sbaConfig.getClasswork_scale())/100);
                        examsEntry.setPosition(score.getPosition());
                        examsEntry.setPercent_of_total_exams(score.getMarks() *
                                (100-sbaConfig.getClasswork_scale())/100);
                        examsEntryList.add(examsEntry);
                        break;
                    }
                }
            }
        } else if (examsScores.isEmpty()){
            for (Student student : studentList){
                for (SBA sba : sbaList){
                    if (student.equals(sba.getMarksId().getStudent_id())){
                        ExamsEntry examsEntry1 = getExamsEntry(student, sba,sbaConfig);
                        examsEntry1.setTotal_exam_marks(examsEntry1.getTotal_sba());
                        examsEntryList.add(examsEntry1);
                        break;
                    }
                }
            }
            Help.findPosition(examsEntryList);
        }else {
            for (Student student : studentList){
               for(ExamsScore score : examsScores){
                   for (SBA sba : sbaList){
                       if (student.equals(score.getExams_score_id().getStudent_id())
                               && student.equals(sba.getMarksId().getStudent_id())
                       ){
                           examsEntry = getExamsEntry(student, sba,sbaConfig);
                           examsEntry.setTotal_exam_marks(examsEntry.getTotal_sba() +(
                                   score.getMarks()* (100-sbaConfig.getClasswork_scale())/100));
                           examsEntry.setPosition(score.getPosition());
                           examsEntry.setExamsScore(score.getMarks());
                           examsEntry.setPercent_of_total_exams(score.getMarks() *
                                   (100-sbaConfig.getClasswork_scale())/100);
                           examsEntryList.add(examsEntry);
                           break;
                       }
                   }
               }
            }
        }
        if (term_id.intValue() == 1)
            previous = examsScoreInterface.examsScores(classes,subjects,termInterface.findTermById(2L));
        else if(term_id.intValue() == 2)
            previous = examsScoreInterface.examsScores(classes,subjects,termInterface.findTermById(3L));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Classes> classesList = classesInterface.findClassByDepartment(teacher.getDepartment_Id());
        String teacher_class_id = "";
        for (Classes classes1 : classesList){
            Optional<Teacher> optional = Optional.ofNullable(classes1.getClass_teacher_id());
            if (optional.isPresent() && classes1.getClass_teacher_id().equals(teacher))
                teacher_class_id = classes1.getClass_id();
        }
        exams_entrySet.setExamsEntries(examsEntryList);
        model.addAttribute("term",term);
        model.addAttribute("subject",subjects);
        model.addAttribute("class",classes);
        model.addAttribute("entry_list",exams_entrySet);
        model.addAttribute("sbaConfig", sbaConfig);
        model.addAttribute("class_id",teacher_class_id);
        assert previous != null;
        if (previous.size() != 0)
            return "exams_entry_view_page";
        else if(! teacher_class_id.equals(""))
            return "exams_entry_page_class_teacher";
        return "exams_entry_page";
    }
    private ExamsEntry getExamsEntry(Student student, SBA sba ,SBAConfig sbaConfig) {
        ExamsEntry examsEntry;
        Optional<Double> value = sba.getMarks().stream().reduce(Double :: sum);
        examsEntry = new ExamsEntry();
        examsEntry.setStudentId(student.getStudent_id());
        examsEntry.setStudentFull_name(student.getStudentFullName());
        examsEntry.setTotal_sba(value.orElse(0.00) * ((double)sbaConfig.getClasswork_scale()/100));
        examsEntry.setGrade(Help.grade(examsEntry.getTotal_sba()));
        return examsEntry;
    }

    @RequestMapping(value = "/save_exams_entry")
    public String saveExamsMarks(@ModelAttribute("entry_list") Exams_EntrySet exams_entrySet,
                                 HttpServletRequest servletRequest){
        List<ExamsEntry> examsEntryList = exams_entrySet.getExamsEntries();
        List<ExamsScore> examsScoreList = new ArrayList<>();
        ExamsScore examsScore;
        Subjects subjects = subjectInterface.findSubjectById(Long.parseLong(
                servletRequest.getParameter("subj_id")
        ));
        Classes classes = classesInterface.findClassById(servletRequest.getParameter("cls_id"));
        Term term = termInterface.findTermById(Long.parseLong(servletRequest.getParameter("trm_id")));
        Help.findPosition(examsEntryList);
        for (ExamsEntry examsEntry : examsEntryList){
            Student student = studentInterface.findStudentById(examsEntry.getStudentId());
            MarksId marksId = new MarksId(student,classes,term,subjects);
            examsScore = new ExamsScore(marksId,examsEntry.getExamsScore(),examsEntry.getPosition(),
                    academicYearInterface.findAcademicYearByMaxId(Status.CURRENT));
            examsScoreList.add(examsScore);
        }
        examsScoreInterface.saveAllExamsScore(examsScoreList);
        return "redirect:/exams_page?cls="+classes.getClass_id()+"&trm="+term.getTerm_id()+"&sub="+
                subjects.getSubject_id();
    }

    @RequestMapping(value = "/teacher_sba_entry_form")
    public String teacher_sba_entry_form(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Subjects> teacherSubjects = subjectInterface.findSubjectByTeacher(teacher);
        List<Classes> classes = classesInterface.classList();
        List<Classes> teacherClasses = new ArrayList<>();
        Long class_teacher_id = 0L;
        for (Classes classes1 : classes){
            Optional<Teacher> optional = Optional.ofNullable(classes1.getClass_teacher_id());
            if (optional.isEmpty())
                class_teacher_id = 0L;
            else{
                if (classes1.getClass_teacher_id().equals(teacher))
                    class_teacher_id = teacher.getTeacher_id();
            }
        }
        for (Classes classes1 : classes){
            for (Teacher teacher1 : classes1.getClass_teachers()){
                if (teacher1.equals(teacher)){
                    teacherClasses.add(classes1);
                }
            }
        }
        model.addAttribute("classes", teacherClasses);
        model.addAttribute("subjects", teacherSubjects);
        if(teacherSubjects.size() ==1 && class_teacher_id.intValue() == 0)
            return "sba_home_page_one_sub";
        else if(teacherSubjects.size() ==1 && class_teacher_id.intValue() > 0)
            return "sba_home_page_class_teacher_one_sub";
        else if(teacherSubjects.size() > 1 && class_teacher_id.intValue() == 0)
            return "sba_home_page_two_sub";
        else
            return "sba_home_page_class_teacher_two";
    }

    @RequestMapping(value = "/teacher_home_page")
    public String teacher_home_page(){
        return "teacher_home_page";
    }

    @RequestMapping(value = "sba_page")
    public String sba_page(HttpServletRequest servletRequest,Model model){
        SbaEntry sbaEntry;
        Sba_EntrySet sba_entrySet = new Sba_EntrySet();
        List<SbaEntry> sbaEntries = new ArrayList<>();
        Long term_id = Long.parseLong(servletRequest.getParameter("trm"));
        Long subject_id = Long.parseLong(servletRequest.getParameter("sub"));
        String class_id = servletRequest.getParameter("cls");
        Classes classes = classesInterface.findClassById(class_id);
        Subjects subjects = subjectInterface.findSubjectById(subject_id);
        Term term = termInterface.findTermById(term_id);
        List<Student> studentList;
        List<SBA> sbaList = sbaInterface.sbaList(classes,subjects,term);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        double [] class_work = new double[sbaConfig.getNumber_of_classwork_columns()];
        List<SBA> previous_list = null;
        if (subjects.getOptions().equals(SubjectOptions.OPTIONAL)) {
            studentList = new ArrayList<>((Collection) subjects.getStudentSet().stream().filter(
                    student -> student.getStudent_status().equals(Student_Status.ACTIVE) &&
                            student.getClasses().equals(classes)
            ));
        }else {
            studentList =  studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        }
        if(sbaList.isEmpty()){
            for (Student student : studentList) {
                sbaEntry = new SbaEntry();
                sbaEntry.setFull_name(student.getStudentFullName());
                sbaEntry.setStudent_id(student.getStudent_id());
                for (int i = 0; i < sbaConfig.getNumber_of_classwork_columns(); i++) {
                    class_work[i] = 0.0;
                }
                sbaEntry.setClass_work(class_work);
                sbaEntries.add(sbaEntry);
            }

        }else {
            int index = 0;
            for (SBA sba : sbaList){
                for (Student student : studentList){
                    if(sba.getMarksId().getStudent_id().equals(student)){
                        for (Double marks : sba.getMarks()){
                            class_work[index] = marks;
                            index++;
                        }
                        Optional<Double> totals = sba.getMarks().stream().reduce(Double::sum);
                        sbaEntry = new SbaEntry();
                        sbaEntry.setFull_name(student.getStudentFullName());
                        sbaEntry.setStudent_id(student.getStudent_id());
                        sbaEntry.setTotal(totals.orElse(0.0));
                        sbaEntry.setPercent_of_total_sba(sbaEntry.getTotal()*(double)
                        sbaConfig.getClasswork_scale()/100);
                        sbaEntry.setClass_work(class_work);
                        sbaEntries.add(sbaEntry);
                        class_work = new double[sbaConfig.getNumber_of_classwork_columns()];
                        index = 0;
                    }
                }
            }
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Classes> classesList = classesInterface.findClassByDepartment(teacher.getDepartment_Id());
        String teacher_class_id = "";
        for (Classes classes1 : classesList){
            Optional<Teacher> optional = Optional.ofNullable(classes1.getClass_teacher_id());
            if (optional.isPresent() && classes1.getClass_teacher_id().equals(teacher))
                teacher_class_id = classes1.getClass_id();
        }
        sba_entrySet.setSbaEntries(sbaEntries);
        model.addAttribute("sba_list",sba_entrySet);
        model.addAttribute("term",term);
        model.addAttribute("subject",subjects);
        model.addAttribute("class",classes);
        model.addAttribute("sba_config",sbaConfig);
        model.addAttribute("class_id",teacher_class_id);
        if(term_id.intValue() == 1)
            previous_list = sbaInterface.sbaList(classes,subjects,termInterface.findTermById(2L));
        else if(term_id.intValue() ==2)
            previous_list = sbaInterface.sbaList(classes,subjects,termInterface.findTermById(3L));
        if(previous_list.size() != 0)
            return "teacher_sba_view_page";
        else if(! teacher_class_id.equals(""))
            return "teacher_sba_page_class_teacher";
        return "teacher_sba_page";
    }

    @RequestMapping(value = "/save_sba_entry")
    public String saveSbaEntry(@ModelAttribute("sba_list") Sba_EntrySet sba_entrySet,
                               HttpServletRequest servletRequest){
        List<SbaEntry> entries = sba_entrySet.getSbaEntries();
        List<SBA> sbaList = new ArrayList<>();
        SBA sba;
        Subjects subjects = subjectInterface.findSubjectById(Long.parseLong(
                servletRequest.getParameter("subj_id")
        ));
        Classes classes = classesInterface.findClassById(servletRequest.getParameter("cls_id"));
        Term term = termInterface.findTermById(Long.parseLong(servletRequest.getParameter("trm_id")));
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        for(SbaEntry sbaEntry : entries){
            Student student = studentInterface.findStudentById(sbaEntry.getStudent_id());
            MarksId marksId = new MarksId(student,classes,term,subjects);
            List<Double> marks = new ArrayList<>();
            for (Double mark : sbaEntry.getClass_work()){
                marks.add(mark);
            }
            sba = new SBA(marksId, marks,year,sbaConfig);
            sbaList.add(sba);
        }
        sbaInterface.saveAllSbaList(sbaList);
        return "redirect:/sba_page?cls="+classes.getClass_id()+"&trm="+term.getTerm_id()+"&sub="
                + subjects.getSubject_id();
    }
    @RequestMapping(value = "/terminal_report")
    public String terminalReport(@RequestParam("trm")Long term_id,@RequestParam("cls")
                                 String class_id ,Model model){
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        SubjectReportSummary summary ;
        List<SubjectReportSummary> summaryList = new ArrayList<>();
        Report report = null;
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        if (sbaConfig == null){
            sbaConfig = new SBAConfig();
        }
        int scale = sbaConfig.getClasswork_scale();
        double cls_work;
        double exa_marks;
        List<Student> student = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        List<ExamsScore> scoreList = examsScoreInterface.findExamsScoreByClassAndTerm(classes,term);
        List<SBA> sbaList = sbaInterface.findSbaByClassAndTerm(classes,term);
        List<Subjects> subjects = subjectInterface.subjectByDepartment(classes.getParentClass().getClass_department(),
                Student_Status.ACTIVE);
        List<ReportDetails> reportDetails = reportDetailInterface.findReportDetailByClassAndTerm(classes,term);
        for (Student student1 : student){
            for (Subjects subjects1 : subjects){
                summary = new SubjectReportSummary();
                report = new Report();
                if (subjects1.getOptions().equals(SubjectOptions.OPTIONAL) && subjects1.getStudentSet().contains(student1)) {
                    summary.setSubject_name(subjects1.getSubject_name());
                }else if (subjects1.getOptions().equals(SubjectOptions.OPTIONAL) && ! subjects1.getStudentSet().contains(student1)){
                    continue;
                }else {
                    summary.setSubject_name(subjects1.getSubject_name());
                }

                summaryList.add(summary);
            }
            assert report != null;
            report.setStudent(student1);
            report.setFull_name(report.getStudent().getStudentFullName());
            summaryList.sort(Comparator.comparing(SubjectReportSummary::getSubject_name));
            report.setSummaryList(summaryList);
            reportList.add(report);
            summaryList = new ArrayList<>();
        }
        for(ExamsScore score : scoreList){
            for (Report report1 : reportList){
                if (score.getExams_score_id().getStudent_id().equals(report1.getStudent())){
                    for (SubjectReportSummary summary1 : report1.getSummaryList()){
                        if(summary1.getSubject_name().equals(score.getExams_score_id().getSubject_id().getSubject_name())){
                            exa_marks = score.getMarks() * (100 - scale)/100.0;
                            summary1.setExams_score(exa_marks);
                            summary1.setTotal(exa_marks);
                            summary1.setPosition(score.getPosition());
                        }
                    }
                   break;
                }
            }
        }
        for (SBA sba : sbaList){
            for (Report report1 : reportList){
                if(sba.getMarksId().getStudent_id().equals(report1.getStudent())){
                    for (SubjectReportSummary summary1 : report1.getSummaryList()){
                        if (summary1.getSubject_name().equals(sba.getMarksId().getSubject_id().getSubject_name())){
                            Optional<Double> totals = sba.getMarks().stream().reduce(Double::sum);
                            cls_work = totals.orElse(0.0) * (scale/100.0);
                            summary1.setClass_score(cls_work);
                            summary1.setTotal(summary1.getTotal()+cls_work);
                            summary1.setRemarks(Help.remarks(summary1.getTotal()));
                            summary1.setGrade(Help.grade(summary1.getTotal()));
                            report1.setTotal_score(report1.getTotal_score() + summary1.getTotal());
                        }
                    }
                    report1.setAverage_score(report1.getAverage_score()/(100.0*subjects.size()));
                    break;
                }
            }
        }
        for (Report report1 : reportList){
            for (ReportDetails details : reportDetails){
                if(report1.getStudent().equals(details.getReportDetailsId().getStudent_id())){
                    report1.setReportDetails(details);
                    break;
                }
            }
            report1.setReportDetails(new ReportDetails());

        }
        Help.findPositionInClass(reportList);
        Help.aggregateScore(reportList);
        report = reportList.get(0);
        model.addAttribute("num_on_roll",reportList.size());
        setModels(model,report,classes,term,1);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        for (Subjects subjects1 : subjectInterface.findSubjectByTeacher(teacher)){
            if (subjects1.getOptions().equals(SubjectOptions.OPTIONAL) &&
                ! subjects1.getStudentSet().contains(report.getStudent())){
                return "progress_report_no_sub";
            }
        }

        return "progress_report";
    }

    @RequestMapping(value = "/next")
    public String next(Model model,@RequestParam("trm") Long term_id,
                       @RequestParam("cls")String class_id,@RequestParam("index") Integer index) throws AppExceptions{
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Report report;
        if (index > reportList.size()-1){
            String message = "END OF STUDENT RECORD IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else
            report = reportList.get(index);
        model.addAttribute("num_on_roll",reportList.size());
        setModels(model,report,classes,term,++index);
        return "progress_report";
    }
    private void setModels(Model model, Report report, Classes classes,Term term, Integer index){
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        CommonReportDetails common = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
        common = (common != null) ? common : new CommonReportDetails();
        SBAConfig config = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        config = (config == null) ? new SBAConfig() : config;
        ReportDetails details = new ReportDetails();
        model.addAttribute("report",report);
        model.addAttribute("class",classes);
        model.addAttribute("academic_year",year);
        model.addAttribute("term",term);
        model.addAttribute("config",config);
        model.addAttribute("index",index);
        model.addAttribute("commonDetails",common);
        model.addAttribute("conduct", conductInterface.findAllConducts());
        model.addAttribute("remarks",remarksInterface.allRemarks());
        model.addAttribute("interest", interestInterface.findAllInterest());
        model.addAttribute("reportDetail", details);
        model.addAttribute("num_on_roll",reportFillingList.size());
    }

    @RequestMapping(value = "/prev")
    public String prev(Model model,@RequestParam("trm") Long term_id,
                       @RequestParam("cls")String class_id,@RequestParam("index") Integer index) throws AppExceptions{
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Report report;
        if (index <= 0){
            String message = "END OF STUDENT RECORD IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else
            report = reportList.get(--index);
        model.addAttribute("num_on_roll",reportList.size());
        setModels(model,report,classes,term,index);
        return "progress_report";
    }

    @RequestMapping(value = "/report_filling_form")
    public String reportFillingForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Classes> classesList = classesInterface.findClassByDepartment(teacher.getDepartment_Id());
        String teacher_class_id = "";
        for (Classes classes1 : classesList){
            Optional<Teacher> optional = Optional.ofNullable(classes1.getClass_teacher_id());
            if (optional.isPresent() && classes1.getClass_teacher_id().equals(teacher))
                teacher_class_id = classes1.getClass_id();
        }
        model.addAttribute("class_id", teacher_class_id);
        return "filling_report_form";
    }
    @RequestMapping(value = "/report_filling")
    public String reportFilling(@RequestParam("cls") String class_id,
                                @RequestParam("trm") Long term_id,Model model){
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        SubjectReportSummary summary ;
        List<SubjectReportSummary> summaryList = new ArrayList<>();
        Report report = null;
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        int scale = sbaConfig.getClasswork_scale();
        double cls_work;
        double exa_marks;
        List<Student> student = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        List<ExamsScore> scoreList = examsScoreInterface.findExamsScoreByClassAndTerm(classes,term);
        List<SBA> sbaList = sbaInterface.findSbaByClassAndTerm(classes,term);
        List<Subjects> subjects = subjectInterface.subjectByDepartment(classes.getParentClass().getClass_department(),
                Student_Status.ACTIVE);
        List<ReportDetails> reportDetails = reportDetailInterface.findReportDetailByClassAndTerm(classes,term);
        CommonReportDetails commonDetails = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
        if (commonDetails == null) {
            CommonReportDetails common = new CommonReportDetails(
                    Help.signature(classes.getParentClass().getClass_department().getHead().getFirstName(),
                            classes.getParentClass().getClass_department().getHead().getLastName()),
                    Help.signature(classes.getClass_teacher_id().getFirst_name(), classes.getClass_teacher_id().getLast_name()),
                    Help.termBegins(term, termInfoInterface, academicYearInterface).getOpen_date(),
                    year, classes,term,student.size());
            commonDetailsInterface.saveCommonDetails(common);
        }
        for (Student student1 : student){
            for (Subjects subjects1 : subjects){
                summary = new SubjectReportSummary();
                report = new Report();
                if (subjects1.getOptions().equals(SubjectOptions.OPTIONAL) && subjects1.getStudentSet().contains(student1)) {
                    summary.setSubject_name(subjects1.getSubject_name());
                }else if (subjects1.getOptions().equals(SubjectOptions.OPTIONAL) && ! subjects1.getStudentSet().contains(student1)){
                    continue;
                }else {
                    summary.setSubject_name(subjects1.getSubject_name());
                }
                summaryList.add(summary);
            }
            assert report != null;
            report.setStudent(student1);
            report.setFull_name(report.getStudent().getStudentFullName());
            summaryList.sort(Comparator.comparing(SubjectReportSummary::getSubject_name));
            report.setSummaryList(summaryList);
            reportFillingList.add(report);
            summaryList = new ArrayList<>();
        }
        for(ExamsScore score : scoreList){
            for (Report report1 : reportFillingList){
                if (score.getExams_score_id().getStudent_id().equals(report1.getStudent())){
                    for (SubjectReportSummary summary1 : report1.getSummaryList()){
                        if(summary1.getSubject_name().equals(score.getExams_score_id().getSubject_id().getSubject_name())){
                            exa_marks = score.getMarks() * (100 - scale)/100.0;
                            summary1.setExams_score(exa_marks);
                            summary1.setTotal(exa_marks);
                            summary1.setPosition(score.getPosition());
                        }
                    }
                    break;
                }
            }
        }
        for (SBA sba : sbaList){
            for (Report report1 : reportFillingList){
                if(sba.getMarksId().getStudent_id().equals(report1.getStudent())){
                    for (SubjectReportSummary summary1 : report1.getSummaryList()){
                        if (summary1.getSubject_name().equals(sba.getMarksId().getSubject_id().getSubject_name())){
                            Optional<Double> totals = sba.getMarks().stream().reduce(Double::sum);
                            cls_work = totals.orElse(0.0) * (scale/100.0);
                            summary1.setClass_score(cls_work);
                            summary1.setTotal(summary1.getTotal()+cls_work);
                            summary1.setRemarks(Help.remarks(summary1.getTotal()));
                            summary1.setGrade(Help.grade(summary1.getTotal()));
                            report1.setTotal_score(report1.getTotal_score() + summary1.getTotal());
                        }
                    }
                    report1.setAverage_score(report1.getAverage_score()/(100.0*subjects.size()));
                    break;
                }
            }
        }
        for (Report report1 : reportFillingList){
            for (ReportDetails details : reportDetails){
                if(report1.getStudent().equals(details.getReportDetailsId().getStudent_id())){
                    report1.setReportDetails(details);
                    break;
                }
            }
            report1.setReportDetails(new ReportDetails());

        }
        Help.findPositionInClass(reportFillingList);
        Help.aggregateScore(reportFillingList);
        report = reportFillingList.get(0);

        setModels(model,report,classes,term,1);
        return "class_teacher_fill_report";
    }

    @RequestMapping(value = "/save_report_details")
    public String saveReportDetails(@ModelAttribute("reportDetails") ReportDetails details,
                                    @RequestParam("stu") String student_id,@RequestParam("trm")
                                    Long term_id,@RequestParam("cls") String class_id,
                                    @RequestParam("idx") Integer index,@RequestParam("pos_in_clas") String pos_in_class
                                    ,@ModelAttribute("commonDetails") CommonReportDetails commonDetails){
        Student student = studentInterface.findStudentById(student_id);
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        ReportDetailsId id = new ReportDetailsId(student,classes,term);
        details.setReportDetailsId(id);
        details.setPosition_in_class(pos_in_class);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        details.setAcademic_year(year);
        CommonReportDetails common;
        if (commonDetails.getTotal_attendance() != 0){
            common = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
            common.setTotal_attendance(commonDetails.getTotal_attendance());
            commonDetailsInterface.updateCommonReportDetails(common.getTotal_attendance(),common.getCommon_Details_id());
        }
        reportDetailInterface.saveDetails(details);
        Help.saveRemark(details.getTeacher_remark(),remarksInterface);
        Help.saveInterest(details.getInterest(),interestInterface);
        Help.saveConduct(details.getAttitude(),conductInterface);
        return "redirect:/next_report_filling?cls="+classes+"trm="+term+"idx="+index;
    }

    @RequestMapping(value = "/next_report_filling")
    public String nextReportFilling(@RequestParam("cls") String class_id,@RequestParam("trm")
                                    Long term_id, @RequestParam("index") Integer index,Model model) throws AppExceptions{
        Report report;
        Classes classes = classesInterface.findClassById(class_id);
        if (index > reportFillingList.size()-1){
            String message = "END OF STUDENT RECORD IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else
            report = reportFillingList.get(index);
        Term term = termInterface.findTermById(term_id);
        setModels(model,report,classes,term,++index);
        return "class_teacher_fill_report";
    }
    @RequestMapping(value = "/prev_report_filling")
    public String prevReportFilling(@RequestParam("cls") String class_id,@RequestParam("trm")
                                    Long term_id, @RequestParam("index") Integer index,Model model) throws AppExceptions{
        Report report;
        Classes classes = classesInterface.findClassById(class_id);
        if (index <= 0){
            String message = "END OF STUDENT RECORD IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else
            report = reportFillingList.get(--index);

        Term term = termInterface.findTermById(term_id);
        setModels(model,report,classes,term,index);
        return "class_teacher_fill_report";
    }
    // throw exception
    @RequestMapping(value = "/passed_record/{student_id}")
    public String passedRecord(@PathVariable(value = "student_id") String student_id,Model model) throws AppExceptions {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        Student student = studentInterface.findStudentById(student_id);
        List<ExamsScore> scoreFromExamsScore;
        List<ExamsScore> scoreFromRecordExamsScore = examsScoreInterface.examsScoreByStudentIdFromRecordExams(student,
                student.getClasses().getParentClass().getClass_department());
        List<SBA> sbaFromSBA;
        List<SBA> sbaFromRecordSBA = sbaInterface.sbaByStudentIdFromRecordSBA(student,
                student.getClasses().getParentClass().getClass_department());
        List<ReportDetails> details = reportDetailInterface.detailsByStudentId(student);
        CommonReportDetails commonReportDetails = new CommonReportDetails();
        List<SubjectReportSummary> summaryList = new ArrayList<>();
        List<TempSubjectSummary> subjectSummaries = new ArrayList<>();
        SubjectReportSummary summary;
        SBAConfig sbaConfig = new SBAConfig();
        Report report;
        double totalScore = 0;
        ReportDetails details1 = new ReportDetails();
        if (student.getStudent_status().equals(Student_Status.COMPLETED) && scoreFromRecordExamsScore.isEmpty()){
            String message =
                    """
                    STUDENT HAS NO PASSED RECORD
                    """;
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,message));
        }
        if (student.getStudent_status().equals(Student_Status.ACTIVE)){
            scoreFromExamsScore = examsScoreInterface.examsScoreByStudentIdFromExams(student,
                    student.getClasses().getParentClass().getClass_department());
            sbaFromSBA = sbaInterface.sbaByStudentIdFromSBA(student,student.getClasses().getParentClass().getClass_department());
            if (scoreFromExamsScore.isEmpty() || scoreFromRecordExamsScore.isEmpty()){
                String message =
                        """
                        STUDENT HAS NO PASSED RECORD
                        """;
                throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,message));
            }
            for(ExamsScore examsScore : scoreFromExamsScore){
                for (SBA sba : sbaFromSBA){
                    if (sba.getMarksId().equals(examsScore.getExams_score_id())){
                        subjectSummaries.add(new TempSubjectSummary(examsScore,sba));
                    }
                }
            }
        }
        for(ExamsScore examsScore : scoreFromRecordExamsScore){
            for (SBA sba : sbaFromRecordSBA){
                if (sba.getMarksId().equals(examsScore.getExams_score_id())){
                    subjectSummaries.add(new TempSubjectSummary(examsScore,sba));
                }
            }
        }
        for (int i = 0; i < subjectSummaries.size(); i++) {
            for (int j = i+1; j < subjectSummaries.size() ; j++) {
                Department department;Academic_Year year;Term term;
                double examsScore,classScore;
                if ((!subjectSummaries.get(i).equals(new TempSubjectSummary())|| !subjectSummaries.get(j).equals(new TempSubjectSummary())) &&
                        (subjectSummaries.get(i).getExamsScore().getExams_score_id().getTerm_id().equals(
                        subjectSummaries.get(j).getExamsScore().getExams_score_id().getTerm_id()))&&
                        (subjectSummaries.get(i).getExamsScore().getExams_score_id().getClass_id().equals(
                                subjectSummaries.get(j).getExamsScore().getExams_score_id().getClass_id()))

                ){
                    department = subjectSummaries.get(i).getExamsScore().getExams_score_id().getClass_id().getParentClass().getClass_department();
                    year = subjectSummaries.get(i).getExamsScore().getAcademic_year();
                    term = subjectSummaries.get(i).getExamsScore().getExams_score_id().getTerm_id();
                    sbaConfig = sbaConfigInterface.findSBAConfigByDepartmentAndYear(department,year);
                    Optional<Double> classScoreTotal = subjectSummaries.get(j).getClassScore().getMarks().stream().reduce(
                            Double::sum);
                    examsScore = subjectSummaries.get(j).getExamsScore().getMarks()*(100-sbaConfig.getClasswork_scale())/100;
                    classScore = classScoreTotal.orElse(0.0) * sbaConfig.getClasswork_scale()/100;
                    summary = new SubjectReportSummary(
                            subjectSummaries.get(j).getExamsScore().getExams_score_id().getSubject_id().getSubject_name(),
                            examsScore,classScore,(examsScore+classScore),subjectSummaries.get(j).getExamsScore().getPosition(),
                            Help.remarks(examsScore+classScore),Help.grade(examsScore+classScore)
                    );
                    totalScore = totalScore + (examsScore+classScore);
                    commonReportDetails = commonDetailsInterface.findDetailsByClass_Term_Year(year,
                            subjectSummaries.get(j).getExamsScore().getExams_score_id().getClass_id(), term);
                    summaryList.add(summary);
                    if (!details1.equals(new ReportDetails())) {
                        for (ReportDetails reportDetails : details) {
                            if (reportDetails.getReportDetailsId().getTerm_id().equals(subjectSummaries.get(j).getExamsScore().getExams_score_id().getTerm_id()) &&
                                    reportDetails.getReportDetailsId().getClass_id().equals(subjectSummaries.get(j).getExamsScore().getExams_score_id().getClass_id())) {
                                details1 = reportDetails;
                            }
                        }
                    }
                    subjectSummaries.set(j,new TempSubjectSummary());
                }
            }
            report = new Report(student,student.getStudentFullName(),
                    totalScore,0,totalScore/ summaryList.size(),details1,"",summaryList);
            passReportList.add(report);
            Help.aggregateScore(passReportList);
            summaryList = new ArrayList<>();
            int index = 0;
            setModelForPassRecord(commonReportDetails,model,passReportList.get(index),sbaConfig,index+1);
        }
        for (Subjects subjects : subjectInterface.findSubjectByTeacher(teacher)){
            if (subjects.getOptions().equals(SubjectOptions.OPTIONAL)
                    && ! subjects.getStudentSet().contains(student) ){
                return "pass_report_no_sub";
            }
        }
        return "pass_report";
    }

    private void setModelForPassRecord(CommonReportDetails details,Model model,Report report,
            SBAConfig config,Integer index){
        model.addAttribute("common",details);
        model.addAttribute("report",report);
        model.addAttribute("class",report.getReportDetails().getReportDetailsId().getClass_id());
        model.addAttribute("term",report.getReportDetails().getReportDetailsId().getTerm_id());
        model.addAttribute("config",config);
        model.addAttribute("index",index);
    }

    private void passReportNextPrev(Report report,Model model,Integer index){
        Department department = report.getReportDetails().getReportDetailsId().getClass_id().getParentClass().getClass_department();
        Academic_Year year = report.getReportDetails().getAcademic_year();
        Term term = report.getReportDetails().getReportDetailsId().getTerm_id();
        Classes classes = report.getReportDetails().getReportDetailsId().getClass_id();
        CommonReportDetails details = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
        SBAConfig config = sbaConfigInterface.findSBAConfigByDepartmentAndYear(department,year);
        setModelForPassRecord(details,model,report,config,index);
    }

    @RequestMapping(value = "/prev_pass_report")
    public String prev_pass_report(@RequestParam("index") Integer index,Model model) throws AppExceptions{
        Report report;
        if (index <=0){
            String message = "END OF STUDENT RECORD";
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else {
            report = passReportList.get(--index);
        }
        passReportNextPrev(report,model,index);
        return "pass_report";
    }

    @RequestMapping(value = "/next_pass_report")
    public String next_pass_report(@RequestParam("index") Integer index,Model model) throws AppExceptions{
        Report report;
        if (index > passReportList.size()-1){
            String message = "END OF STUDENT RECORD";
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else {
            report = passReportList.get(index);
        }
        passReportNextPrev(report,model,++index);
        return "pass_report";
    }

    @RequestMapping(value = "/record_form")
    public String recordForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Classes> classesList = classesInterface.findClassByDepartment(teacher.getDepartment_Id());
        model.addAttribute("class",classesList);
        return "record_form";
    }

    @RequestMapping(value = "/student_list")
    public String studentReportList(HttpServletRequest request,Model model){
        Classes classes = classesInterface.findClassById(request.getParameter("cls"));
        List<Student> studentList = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        for (Student student: studentList){
            student.setFullName(student.getStudentFullName());
        }
        model.addAttribute("classStudents",studentList);
        model.addAttribute("class",classes);
        return "student_report_list";
    }

    @RequestMapping(value = "/students_list_form")
    public String studentListForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        List<Classes> classesList = classesInterface.findClassByDepartment(teacher.getDepartment_Id());
        model.addAttribute("class",classesList);
        return "class_form_list";
    }

    @RequestMapping(value = "/student_list_profile")
    public String studentListProfile(@RequestParam("cls") String class_id,Model model){
        Classes classes = classesInterface.findClassById(class_id);
        List<Student> studentList = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        for (Student student: studentList){
            student.setFullName(student.getStudentFullName());
        }
        model.addAttribute("classStudents",studentList);
        model.addAttribute("class",classes);
        return "student_profile_list";
    }

    @RequestMapping(value = "/view_profile")
    public String viewStudentProfile(@RequestParam("stu") String student_id,Model model){
        Student student = studentInterface.findStudentById(student_id);
        student.setFullName(student.getStudentFullName());
        Guardian guardian = guardianInterface.findByStudent(student);
        if (guardian == null){
            guardian = new Guardian();
            guardian.setRelationType(new RelationType());
        }else {
            guardian.setGuard_fullName(guardian.getGuardianFullName());
        }
        model.addAttribute("guardian",guardian);
        model.addAttribute("student",student);
        return "view_student_profile";
    }

    // exception on method note!!!!

    @RequestMapping(value = "/search_progress_report")
    public String searchProgressReport(HttpServletRequest servletRequest,Model model) throws AppExceptions{
        Term term = termInterface.findTermById(Long.parseLong(servletRequest.getParameter("trm")));
        Classes classes = classesInterface.findClassById(servletRequest.getParameter("cls"));
        Student student = studentInterface.findStudentById(servletRequest.getParameter("stuId"));
        Integer index = Integer.parseInt(servletRequest.getParameter("idx"));
        Report report = new Report() ;
        boolean found = false;
        for (Report report1 : reportList){
            if (report1.getStudent().equals(student)){
                report = report1;
                found = true;
            }
        }
        if (student == null){
            String message = "STUDENT NOT FOUND";
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,message));
        } else if (!found) {
            String message = "STUDENT IS NOT IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }
        model.addAttribute("num_on_roll",reportList.size());
        setModels(model,report,classes,term,index);
        return "progress_report";
    }
    // exception on method note!!!!

    @RequestMapping(value = "/search_filling_report")
    public String searchFillingReport(HttpServletRequest servletRequest,Model model) throws AppExceptions{
        Term term = termInterface.findTermById(Long.parseLong(servletRequest.getParameter("trm")));
        Classes classes = classesInterface.findClassById(servletRequest.getParameter("cls"));
        Student student = studentInterface.findStudentById(servletRequest.getParameter("stuId"));
        Integer index = Integer.parseInt(servletRequest.getParameter("idx"));
        Report report = new Report();
        boolean found = false;
        for (Report report1 : reportFillingList){
            if (report1.getStudent().equals(student)){
                report = report1;
            }
        }
        if (student == null){
            String message = "STUDENT NOT FOUND";
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,message));
        } else if (!found) {
            String message = "STUDENT IS NOT IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }
        setModels(model,report,classes,term,index);
        return "class_teacher_fill_report";
    }

    // not complete yet
    @RequestMapping(value = "/summary_report/{student_id}")
    public String summaryReport(@PathVariable("student_id") String studentId,Model model) throws AppExceptions{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        Teacher teacher = teacherInterface.findTeacherByUsers(users);
        Student student = studentInterface.findStudentById(studentId);
        List<ExamsScore> examsScores = new ArrayList<>();
        List<ExamsScore> examScoresFromRecordExams = new ArrayList<>();
        List<ProgressReport> progressReportList = new ArrayList<>();
        List<Subjects> subjectsList = subjectInterface.findSubjectByTeacher(teacher);
        for (Subjects subjects :subjectsList) {
            examsScores.addAll(examsScoreInterface.examsScoreByStudentAndSubject(student, subjects));
            examScoresFromRecordExams.addAll(examsScoreInterface.examsScoreByStudentAndSubjectFromRecordExams
                    (student, subjects));
            ProgressReport report = new ProgressReport();
            if (examsScores.isEmpty() && examScoresFromRecordExams.isEmpty())
                throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,
                        "NO RECORD FOUND"));
            else if (examsScores.isEmpty()) {
                for (ExamsScore score : examScoresFromRecordExams) {
                    List<Double> marks;
                    if (report.getGraphData().containsKey(score.getExams_score_id().getClass_id().getClass_name())) {
                        marks = report.getGraphData().get(score.getExams_score_id().getClass_id().getClass_name());
                        marks.add(score.getMarks());
                    } else {
                        marks = new ArrayList<>();
                        marks.add(score.getMarks());
                    }
                    report.getGraphData().put(score.getExams_score_id().getClass_id().getClass_name(), marks);
                }
            } else {
                for (ExamsScore score : examScoresFromRecordExams) {
                    List<Double> marks;
                    if (report.getGraphData().containsKey(score.getExams_score_id().getClass_id().getClass_name())) {
                        marks = report.getGraphData().get(score.getExams_score_id().getClass_id().getClass_name());
                        marks.add(score.getMarks());
                    } else {
                        marks = new ArrayList<>();
                    }
                    report.getGraphData().put(score.getExams_score_id().getClass_id().getClass_name(), marks);
                }
                for (ExamsScore score : examsScores) {
                    List<Double> marks;
                    if (report.getGraphData().containsKey(score.getExams_score_id().getClass_id().getClass_name())) {
                        marks = report.getGraphData().get(score.getExams_score_id().getClass_id().getClass_name());
                        marks.add(score.getMarks());
                    } else {
                        marks = new ArrayList<>();
                    }
                    report.getGraphData().put(score.getExams_score_id().getClass_id().getClass_name(), marks);
                }
            }
            report.getCategories().add("TERM ONE");
            report.getCategories().add("TERM TWO");
            report.getCategories().add("TERM THREE");
            progressReportList.add(report);
        }
        model.addAttribute("graph",progressReportList);
        return "";
    }
}
