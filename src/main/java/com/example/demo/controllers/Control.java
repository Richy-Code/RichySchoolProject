package com.example.demo.controllers;

import com.example.demo.binding_entities.*;
import com.example.demo.entities.*;
import com.example.demo.enum_entities.Gender;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class Control {
    private final UserServiceInterface userServiceInterface;
    private final SubjectInterface subjectInterface;

    private final StudentInterface studentInterface;
    private final ClassesInterface classesInterface;

    private final TermInterface termInterface;

    private final SBAInterface sbaInterface;
    private final SbaConfigInterface sbaConfigInterface;

    private final AcademicYearInterface academicYearInterface;

    private final RolesServiceInterface rolesServiceInterface;

    private final DepartmentInterface departmentInterface;

    private final TeacherInterface teacherInterface;

    private final TermInfoInterface termInfoInterface;
    private final DepartmentHeadInterface departmentHeadInterface;
    private final ConductInterface conductInterface;
    private final InterestInterface interestInterface;
    private final RemarksInterface remarksInterface;
    private final ExamsScoreInterface examsScoreInterface;
    private final ReportDetailInterface reportDetailInterface;
    private final CommonDetailsInterface commonDetailsInterface;

    private final OptionalSubjectInterface optionalSubjectInterface;
    private final PlainPasswordInterface passwordInterface;

    private final ParentClassInterface parentClassInterface;
    private final RelationshipTypeInterface relationshipTypeInterface;

    private final RecordExamsInterface recordExamsInterface;

    private final SbaRecordInterface sbaRecordInterface;

    private final GuardianInterface guardianInterface;
    private final List<Report> reportList = new ArrayList<>();
    private final List<Report> passReportList = new ArrayList<>();
    public Control(UserServiceInterface userServiceInterface, SubjectInterface subjectInterface,
                   StudentInterface studentInterface, ClassesInterface classesInterface,
                   TermInterface termInterface, SBAInterface sbaInterface,
                   SbaConfigInterface sbaConfigInterface, AcademicYearInterface academicYearInterface,
                   RolesServiceInterface rolesServiceInterface,
                   DepartmentInterface departmentInterface, TeacherInterface teacherInterface,
                   TermInfoInterface termInfoInterface,
                   DepartmentHeadInterface departmentHeadInterface,
                   ConductInterface conductInterface, InterestInterface interestInterface,
                   RemarksInterface remarksInterface, ExamsScoreInterface examsScoreInterface,
                   ReportDetailInterface reportDetailInterface,
                   CommonDetailsInterface commonDetailsInterface,
                   OptionalSubjectInterface optionalSubjectInterface,
                   PlainPasswordInterface passwordInterface,
                   ParentClassInterface parentClassInterface,
                   RelationshipTypeInterface relationshipTypeInterface,
                   RecordExamsInterface recordExamsInterface,
                   SbaRecordInterface sbaRecordInterface,
                   GuardianInterface guardianInterface) {
        this.userServiceInterface = userServiceInterface;
        this.subjectInterface = subjectInterface;
        this.studentInterface = studentInterface;
        this.classesInterface = classesInterface;
        this.termInterface = termInterface;
        this.sbaInterface = sbaInterface;
        this.sbaConfigInterface = sbaConfigInterface;
        this.academicYearInterface = academicYearInterface;
        this.rolesServiceInterface = rolesServiceInterface;
        this.departmentInterface = departmentInterface;
        this.teacherInterface = teacherInterface;
        this.termInfoInterface = termInfoInterface;
        this.departmentHeadInterface = departmentHeadInterface;
        this.conductInterface = conductInterface;
        this.interestInterface = interestInterface;
        this.remarksInterface = remarksInterface;
        this.examsScoreInterface = examsScoreInterface;
        this.reportDetailInterface = reportDetailInterface;
        this.commonDetailsInterface = commonDetailsInterface;
        this.optionalSubjectInterface = optionalSubjectInterface;
        this.passwordInterface = passwordInterface;
        this.parentClassInterface = parentClassInterface;
        this.relationshipTypeInterface = relationshipTypeInterface;
        this.recordExamsInterface = recordExamsInterface;
        this.sbaRecordInterface = sbaRecordInterface;
        this.guardianInterface = guardianInterface;
    }
    @RequestMapping("/Richy")
    public String insertData(){
        departmentInterface.saveAllDepartment(Help.departmentList());
        termInterface.saveAllTerm(Help.termList());
        parentClassInterface.saveAllParentClasses(Help.parentClassList(departmentInterface.findAllDepartment()));
        relationshipTypeInterface.saveAllRelation(Help.relationTypes());
        return "hello";
    }

    @RequestMapping("/admin")
    public String adminHomePage(){

        return "admin_home_page";
    }

    @RequestMapping(value = "/academic")
    public String academic(Model model){
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        List<Term_Info> infoList = termInfoInterface.findTermInfoByAcademicYear(year);
        Term_Info termInfo = new Term_Info();
        model.addAttribute("infoList",infoList);
        model.addAttribute("termInfo",termInfo);
        model.addAttribute("year",(year == null) ? new Academic_Year() : year);
        return "academic_setting";
    }
    // throw exception if saving is not successful
    @RequestMapping(value = "/save_academic_setting")
    public String saveAcademicSetting(HttpServletRequest request,
                                      @ModelAttribute("termInfo") Term_Info info)throws AppExceptions{
        Term term = termInterface.findTermById(Long.parseLong(request.getParameter("trm")));
        info.setTerm(term);
        String firstYear = request.getParameter("first_yrs");
        String lastYear = request.getParameter("last_yrs");
        Academic_Year year = academicYearInterface.findByFirstAndLastYear(firstYear,lastYear);
        List<Term_Info> infoList = termInfoInterface.findTermInfoByAcademicYear(year);
        if ((year != null && year.getStatus().equals(Status.CURRENT))
         || year !=null && year.getStatus().equals(Status.PASSED)){
            for (Term_Info termInfo : infoList){
                if (termInfo.getTerm().equals(info.getTerm()))
                    throw new NoPassRecordFoundException(new ErrorMessage(
                            HttpStatus.CONFLICT,"RECORD ALREADY EXIST"
                    ));

            }
            info.setAcademic_year(year);
            termInfoInterface.saveTermInfo(info);
        } else if (year == null && academicYearInterface.findAcademicYearByMaxId(Status.CURRENT)== null) {
            academicYearInterface.saveAcademicYear(new Academic_Year(firstYear,lastYear,Status.CURRENT));
            info.setAcademic_year(academicYearInterface.findAcademicYearByMaxId(Status.CURRENT));
            termInfoInterface.saveTermInfo(info);
        }else {
            academicYearInterface.saveAcademicYear(new Academic_Year(firstYear,lastYear,Status.NEXT));
            info.setAcademic_year(academicYearInterface.findAcademicYearByMaxId(Status.NEXT));
            termInfoInterface.saveTermInfo(info);
        }
        return "redirect:/academic";
    }
    @RequestMapping(value = "/term_report_form")
    public String adminTerminalReport(Model model){
        DepartmentHead departmentHead = findAdmin();
        List<Classes> classes = classesInterface.listClassByDepartmentHead(departmentHead);
        model.addAttribute("classes",classes);
        return "admin_term_report_form";
    }

    private DepartmentHead findAdmin(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String user_name = authentication.getName();
        Users users = userServiceInterface.getUser(user_name);
        return departmentHeadInterface.findHeadByUser(users);
    }

    @RequestMapping(value = "/admin_terminal_report")
    public String adminTerminalReport(HttpServletRequest request, Model model){
        Classes classes = classesInterface.findClassById(request.getParameter("cls"));
        Term term = termInterface.findTermById(Long.parseLong(request.getParameter("trm")));
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        SubjectReportSummary summary ;
        List<SubjectReportSummary> summaryList = new ArrayList<>();
        Report report = null;
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        sbaConfig = (sbaConfig==null)? new SBAConfig() : sbaConfig;
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
        int index = 0;
        report = reportList.get(index);
        setModels(model,report,classes,term,index+1);
        return "head_filling_report";
    }

    private void setModels(Model model, Report report, Classes classes,Term term, Integer index){
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        CommonReportDetails common = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        sbaConfig = (sbaConfig != null) ? sbaConfig : new SBAConfig();
        common = (common != null) ? common : new CommonReportDetails();
        ReportDetails details = new ReportDetails();
        model.addAttribute("report",report);
        model.addAttribute("class",classes);
        model.addAttribute("academic_year",year);
        model.addAttribute("term",term);
        model.addAttribute("config",sbaConfig);
        model.addAttribute("index",index);
        model.addAttribute("commonDetails",common);
        model.addAttribute("conduct", conductInterface.findAllConducts());
        model.addAttribute("remarks",remarksInterface.allRemarks());
        model.addAttribute("interest", interestInterface.findAllInterest());
        model.addAttribute("reportDetail", details);
        model.addAttribute("num_on_roll",reportList.size());
    }

    @RequestMapping(value = "/save_head_filling_report")
    public String saveHeadFillingReport(@ModelAttribute("reportDetail") ReportDetails details,
                                        @RequestParam("cls")String classId,@RequestParam("trm") Long termId,
                                        @RequestParam("idx")Integer index,@RequestParam("stuId")String studId){
        Classes classes = classesInterface.findClassById(classId);
        Term term = termInterface.findTermById(termId);
        Student student = studentInterface.findStudentById(studId);
        ReportDetailsId detailsId = new ReportDetailsId(student,classes,term);
        reportDetailInterface.updateHeadRemarks(details.getHead_remarks(),detailsId);
        return "redirect:/head_next_report?cls="+classes+"trm="+term+"idx="+index;
    }

    @RequestMapping(value = "/head_prev_report")
    public String headPrevReport(@RequestParam("cls") String class_id,@RequestParam("trm")
    Long term_id, @RequestParam("index") Integer index,Model model) throws AppExceptions{
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Report report;
        if (index <= 0){
            String message = "END OF STUDENT RECORD IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else
            report = reportList.get(--index);
        setModels(model,report,classes,term,index);
        return "head_filling_report";
    }

    @RequestMapping(value = "/head_next_report")
    public String headNextReport(@RequestParam("cls") String class_id,@RequestParam("trm")
    Long term_id, @RequestParam("index") Integer index,Model model) throws AppExceptions{
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Report report ;
        if (index > reportList.size()-1){
            String message = "END OF STUDENT RECORD IN "+ classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else {
            report = reportList.get(index);
        }
        setModels(model,report,classes,term,++index);
        return "head_filling_report";
    }

    @RequestMapping(value = "/search_head_filling_report")
    public String searchFillingReport(HttpServletRequest servletRequest,Model model) throws AppExceptions{
        Term term = termInterface.findTermById(Long.parseLong(servletRequest.getParameter("trm")));
        Classes classes = classesInterface.findClassById(servletRequest.getParameter("cls"));
        Student student = studentInterface.findStudentById(servletRequest.getParameter("stuId"));
        Integer index = Integer.parseInt(servletRequest.getParameter("idx"));
        Report report = new Report();
        boolean found = false;
        for (Report report1 : reportList){
            if (report1.getStudent().equals(student)){
                report = report1;
                found = true;
            }
        }
        if (student == null) {
            String message =
                    """
                    STUDENT NOT FOUND
                    """;
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND, message));
        }else if (!found){
            String message = "STUDENT NOT IN "+classes.getClass_name();
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }
        setModels(model,report,classes,term,index);
        return "head_filling_report";
    }

    @RequestMapping(value = "/head_passed_record/{student_id}")
    public String passedRecord(@PathVariable(value = "student_id") String student_id, Model model) throws AppExceptions {
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
            sbaFromSBA = sbaInterface.sbaByStudentIdFromSBA(student,
                    student.getClasses().getParentClass().getClass_department());
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
            report = new Report(student,student.getStudentFullName(), totalScore, 0,
                    totalScore/ summaryList.size(),details1,"",summaryList);
            passReportList.add(report);
            Help.aggregateScore(passReportList);
            summaryList = new ArrayList<>();
            int index = 0;
            setModelForPassRecord(commonReportDetails,model,passReportList.get(index),sbaConfig,index+1);
        }
        return "head_progress_report";
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

    @RequestMapping(value = "/classes")
    public String classes(Model model){
        DepartmentHead departmentHead = findAdmin();
        List<Classes> classes = classesInterface.listClassByDepartmentHead(departmentHead);
        List<Integer> studentNumber = new ArrayList<>();
        for (Classes cls : classes){
            studentNumber.add(studentInterface.studentNumberByClass(cls));
        }
        model.addAttribute("classes",classes);
        model.addAttribute("studentNumber",studentNumber);
        return "classes";
    }

    @RequestMapping(value = "/addClass")
    public String addClass(Model model){
        Classes classes = new Classes();
        DepartmentHead head = findAdmin();
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        List<Teacher> teacherList = teacherInterface.possibleClassTeachers(departmentList.get(0));
        teacherList.forEach(teacher -> teacher.setFullName(teacher.getStaffFullName()));
        List<ParentClass> parentClasses = new ArrayList<>();
        for (Department department : departmentList){
            parentClasses.addAll(parentClassInterface.findByDepartment(department));
        }
        model.addAttribute("teachers",teacherList);
        model.addAttribute("class",classes);
        model.addAttribute("parentClass",parentClasses);
        model.addAttribute("selectText" ,parentClasses.get(0).getParentClassName());
        return "add_class";
    }

    @RequestMapping(value = "/edit_class")
    public String editClass(@RequestParam("cls") String classId,Model model){
        Classes classes = classesInterface.findClassById(classId);
        classes.getClass_teacher_id().setFullName(classes.getClass_teacher_id().getStaffFullName());
        List<Teacher> teacherList = teacherInterface.teacherByDepartment
                (classes.getParentClass().getClass_department(), Student_Status.ACTIVE);
        List<Classes> classesList = classesInterface.findClassByDepartment
                (classes.getParentClass().getClass_department());
        for (Classes classes1 : classesList){
            teacherList.remove(classes1.getClass_teacher_id());
        }
        teacherList.forEach(teacher -> teacher.setFullName(teacher.getStaffFullName()));
        model.addAttribute("teachers",teacherList);
        model.addAttribute("class",classes);
        return "edit_class";
    }

    @RequestMapping(value = "/save_edited_class")
    public String saveEditedClass(@ModelAttribute("class")Classes classes,
                                  @ModelAttribute("teachers") TeacherSet teacherSet){
        Classes save_edited_class = classesInterface.findClassById(classes.getClass_id());
        save_edited_class.setClass_teacher_id(teacherInterface.findTeacherByID(classes.getClass_teacher_id().getTeacher_id()));
        ParentClass parentClass = parentClassInterface.findParentClassById(classes.getParentClass().getParentClassId());
        save_edited_class.setParentClass(parentClass);
        Set<Teacher> teachers = new HashSet<>();
        for (Teacher teacher : teacherSet.getList()) {
            if (teacher.getSelected().equalsIgnoreCase("yes")) {
                teachers.add(teacherInterface.findTeacherByID(teacher.getTeacher_id()));
            }
        }
        save_edited_class.setClass_teachers(teachers);
        classesInterface.saveClass(save_edited_class);
        return "redirect:/classes";
    }
    @Transactional
    @RequestMapping(value = "/save_class")
    public String saveClass(@ModelAttribute("class")Classes classes){
        classes.setClass_teacher_id(teacherInterface.findTeacherByID(classes.getClass_teacher_id().getTeacher_id()));
        ParentClass parentClass = parentClassInterface.findParentClassById(classes.getParentClass().getParentClassId());
        List<Classes> classesList = classesInterface.classByParentClass(parentClass);
        if (classesList.isEmpty()){
            classes.setClass_name(parentClass.getParentClassName());
        }else {
            for (int i = 0; i < classesList.size(); i++) {
                String name = parentClass.getParentClassName() + " "+
                        Character.toString(65 + (i));
                classesInterface.updateSubClassName(name,classesList.get(i).getClass_id());
            }
            classes.setClass_name(parentClass.getParentClassName() + " "+
                    Character.toString(65 + (classesList.size())));
        }
        classes.setParentClass(parentClass);
        Set<Teacher> teachers = new HashSet<>();
        List<Teacher> teacherList = teacherInterface.teacherByDepartment(parentClass.getClass_department(),Student_Status.ACTIVE);
        if (teacherList != null ) {
            for (Long teacherId : classes.getSelectedTeachers()) {
                for (Teacher teacher : teacherList) {
                    if (Objects.equals(teacherId, teacher.getTeacher_id())) {
                        teachers.add(teacher);
                        break;
                    }
                }
            }
        }
        classes.setClass_teachers(teachers);
        classesInterface.saveClass(classes);

        return "redirect:/classes";
    }

    @RequestMapping(value = "/teacher_to_class")
    public String teachersToClass(@ModelAttribute("class")Classes classes, Model model,
                                  HttpServletRequest request){
        String parentName = request.getParameter("parentName");
        String allTeachers = request.getParameter("assign_tch");
        ParentClass parentClass = parentClassInterface.parentClassByName(parentName);
        List<Classes> classesList = classesInterface.classByParentClass(parentClass);
        classes.setParentClass(parentClass);
        Department department = departmentInterface.findByDepartmentId(parentClass.getClass_department().getDepartmentID());
        if ("1".equals(allTeachers)){
            if (classesList.isEmpty()){
                classes.setClass_name(parentClass.getParentClassName());
            }else {
                for (int i = 0; i < classesList.size(); i++) {
                    String name = parentClass.getParentClassName() + " "+
                            Character.toString(65 + (i));
                    classesInterface.updateSubClassName(name,classesList.get(i).getClass_id());
                }
                classes.setClass_name(parentClass.getParentClassName() + " "+
                        Character.toString(65 + (classesList.size())));
            }
            classes.setClass_teacher_id(teacherInterface.findTeacherByID(classes.getClass_teacher_id().getTeacher_id()));
            classes.setClass_teachers(new HashSet<>(teacherInterface.teacherByDepartment(department,Student_Status.ACTIVE)));
            classesInterface.saveClass(classes);
            return "redirect:/classes";
        }
        TeacherSet teacherList = new TeacherSet(teacherInterface.teacherByDepartment(department,Student_Status.ACTIVE));
        List<Long> selectedTeachers = new ArrayList<>();
        for (Teacher teacher : teacherList.getList()){
            teacher.setTeacherClasses(classesInterface.findClassByTeacher(teacher.getTeacher_id()));
            teacher.setTeacherSubjects(subjectInterface.teacherSubjectName(teacher));
            teacher.setFullName(teacher.getStaffFullName());
            selectedTeachers.add(0L);
        }
        String className;
        if (classesList.isEmpty()){
            className = parentClass.getParentClassName();
        }else {
            className = parentClass.getParentClassName() +" "+ Character.toString(65 + (classesList.size()));
        }
        classes.setSelectedTeachers(selectedTeachers);
        classes.setClass_teachers(new HashSet<>(teacherList.getList()));
        model.addAttribute("class",classes);
        model.addAttribute("className",className);
        return "teachers_to_class";
    }
    @RequestMapping(value = "/admin_view_student")
    public String viewStudents(@RequestParam("cls") String classId, Model model){
        Classes classes = classesInterface.findClassById(classId);
        List<Student> studentList = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        for (Student student : studentList){
            student.setFullName(student.getStudentFullName());
        }
        model.addAttribute("class",classes);
        model.addAttribute("students",studentList);
        return "student_view_page";
    }

    @RequestMapping(value = "/edit_student_profile")
    public String editStudentProfile(@RequestParam("stu")String student_id,Model model){
        Student student = studentInterface.findStudentById(student_id);
        Guardian guardian = guardianInterface.findByStudent(student);
        List<Classes> classesList = classesInterface.findClassByDepartment(student.getClasses().getParentClass().getClass_department());
        model.addAttribute("student",student);
        model.addAttribute("guardian",(guardian == null) ? 0L : guardian.getGuardian_id());
        model.addAttribute("male",Gender.MALE);
        model.addAttribute("female",Gender.FEMALE);
        model.addAttribute("classList",classesList);
        return "edit_student_profile";
    }

    @RequestMapping(value = "/delete_student_profile")
    public String deleteStudentProfile(@RequestParam("stu") String student_id){
        Student student = studentInterface.findStudentById(student_id);
        Guardian guardian = guardianInterface.findByStudent(student);
        student.setStudent_status(Student_Status.COMPLETED);
        studentInterface.saveStudent(student);
        guardianInterface.deleteGuardian(guardian);
        return "redirect:/admin_view_student?cls="+ student.getClasses().getClass_id();
    }

    @RequestMapping(value = "/edited_teacher_to_class")
    public String editedTeachersToClass(@ModelAttribute("class")Classes classes, Model model,
                                  HttpServletRequest request){
        String allOrChoose = request.getParameter("assign_tch");
        ParentClass parentClass = parentClassInterface.findParentClassById(classes.getParentClass().getParentClassId());
        Department department = departmentInterface.findByDepartmentId(parentClass.getClass_department().getDepartmentID());
        if ("1".equals(allOrChoose)){
            Classes updateClass = classesInterface.findClassById(classes.getClass_id());
            updateClass.setClass_teacher_id(teacherInterface.findTeacherByID(
                    classes.getClass_teacher_id().getTeacher_id()));
            updateClass.setClass_teachers(new HashSet<>(teacherInterface.teacherByDepartment(
                    parentClass.getClass_department(),Student_Status.ACTIVE)));
            updateClass.setParentClass(parentClass);
            classesInterface.saveClass(updateClass);
            return "redirect:/classes";
        }
        TeacherSet teacherList = new TeacherSet(teacherInterface.teacherByDepartment(department,Student_Status.ACTIVE));
        for (Teacher teacher : teacherList.getList()){
            teacher.setTeacherClasses(classesInterface.findClassByTeacher(teacher.getTeacher_id()));
            teacher.setTeacherSubjects(subjectInterface.teacherSubjectName(teacher));
            teacher.setFullName(teacher.getStaffFullName());
        }
        model.addAttribute("class",classes);
        model.addAttribute("teachers",teacherList);
        return "edited_teacher_to_class";
    }

    @RequestMapping(value = "/admin_view_profile")
    public String adminViewStudentProfile(@RequestParam("stu") String studentId,Model model){
        Student student = studentInterface.findStudentById(studentId);
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
        return "admin_view_student_profile";
    }

    @RequestMapping(value = "/staffs")
    public String staffs(Model model){
        DepartmentHead head = findAdmin();
        List<Teacher> teacherList = new ArrayList<>();
        List<Classes> classesList = new ArrayList<>();
        List<Department> department = departmentInterface.findDepartmentByHead(head);
        for (Department dept : department){
            teacherList.addAll(teacherInterface.teacherByDepartment(dept,Student_Status.ACTIVE));
            classesList.addAll(classesInterface.findClassByDepartment(dept));
        }
        for (Classes classes : classesList){
            for (Teacher teacher : teacherList){
                if (classes.getClass_teacher_id().equals(teacher)){
                    teacher.setClass_teacher(classes);
                    break;
                }
            }
        }
        teacherList.forEach(teacher -> teacher.setFullName(teacher.getStaffFullName()));
        model.addAttribute("staffs",teacherList);
        return "teachers";
    }

    @RequestMapping(value = "/view_staff_profile")
    public String viewStaffProfile(@RequestParam("staff") Long teacherId,Model model){
        Teacher teacher = teacherInterface.findTeacherByID(teacherId);
        teacher.setTeacherClasses(classesInterface.findClassByTeacher(teacherId));
        teacher.setTeacherSubjects(subjectInterface.teacherSubjectName(teacher));
        teacher.setFullName(teacher.getStaffFullName());
        List<Classes> classesList = classesInterface.findClassByDepartment(teacher.getDepartment_Id());
        Classes classes = new Classes();
        for (Classes classes1 : classesList){
            if (classes1.getClass_teacher_id().equals(teacher)){
                classes = classes1;
                break;
            }
        }
        model.addAttribute("staff",teacher);
        model.addAttribute("class",classes);
        Optional<String> photo = Optional.ofNullable(teacher.getPhoto());
        if(teacher.getPhotoImage() != null)
            return "view_staff_profile";
        else
            return "view_teacher";
    }

    @RequestMapping(value = "/delete_staff")
    public String deleteStaff(@RequestParam("staff") Long teacherId){
        Teacher staff = teacherInterface.findTeacherByID(teacherId);
        staff.setStatus(Student_Status.COMPLETED);
        teacherInterface.saveTeacher(staff);
        return "redirect:/staffs";
    }

    @RequestMapping(value = "/add_staff")
    public String addStaff(Model model,@ModelAttribute("staff") Teacher teacher){
        DepartmentHead head =findAdmin();
        List<Department> departmentList =departmentInterface.findDepartmentByHead(head);
        model.addAttribute("dept",departmentList);
        model.addAttribute("staff", Objects.requireNonNullElseGet(teacher, Teacher::new));
        model.addAttribute("male",Gender.MALE);
        model.addAttribute("female",Gender.FEMALE);
        return "add_teacher";
    }

    @RequestMapping(value = "/save_staff", method = RequestMethod.POST , produces = "application/json")
    public String saveStaff(@Valid @ModelAttribute("staff") Teacher teacher, Model model,
                            @RequestParam(value = "img",required = false) MultipartFile file){
        DepartmentHead head = findAdmin();
        String fileName = "";
        if (file !=null){
            fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            teacher.setPhoto(fileName);
        }
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        List<Subjects> subjectsList = new ArrayList<>();
        List<Classes> classesList = classesInterface.listClassByDepartmentHead(head);
        for (Department department : departmentList) {
            subjectsList.addAll(subjectInterface.subjectByDepartment(department,Student_Status.ACTIVE));
        }
        teacher.setDepartment_Id(departmentInterface.findByDepartmentId(teacher.getDepartment_Id().getDepartmentID()));
        teacher.setStatus(Student_Status.ACTIVE);
        List<Roles> rolesList = rolesServiceInterface.rolesList();
        Set<Roles> rolesSet = new HashSet<>() ;
        for (Roles roles : rolesList){
            if (roles.getRole_name().equalsIgnoreCase("user")){
                rolesSet.add(roles);
            }
        }
        teacher.setUsers(Help.user(teacher,rolesSet));
        int edited = 0;
        Optional<Long> id = Optional.ofNullable(teacher.getTeacher_id());
        if (id.isPresent()){
            teacherInterface.updateTeacher(teacher.getStatus(),teacher.getLast_name(),teacher.getFirst_name(),
                    teacher.getContact(),teacher.getMiddle_name(),teacher.getDepartment_Id(),teacher.getGender(),
                    teacher.getPhoto(), teacher.getTeacher_id());
            edited = 1;
        }else {
            teacherInterface.saveTeacher(teacher);
            passwordInterface.savePassword(new PlainPassword(teacher.getUsers().getPlainPassword(),teacher));
        }
        if (! fileName.equals("")) {
            String upLoadDirectory = "./pictures/staffs_pics/" + teacher.getTeacher_id();
            Help.saveFile(upLoadDirectory, fileName, file);
        }
        model.addAttribute("subjects",subjectsList);
        model.addAttribute("classes",classesList);
        model.addAttribute("staff",teacher);
        model.addAttribute("edited",edited);
        return "class_subjects";
    }

    @RequestMapping(value = "/save_classes_subject")
    public String saveClassSubject(@ModelAttribute("staff") Teacher teacher,@RequestParam("edited")
                                   int edited){
        Teacher teacher1 = teacherInterface.findTeacherByID(teacher.getTeacher_id());
        if (teacher1.getTeacherClasses() != null){
            for (String classId : teacher1.getTeacherClasses()) {
                Classes classes = classesInterface.findClassById(classId);
                classes.getClass_teachers().add(teacher1);
                classesInterface.saveClass(classes);
            }
        }
        if (teacher1.getTeacherSubjects() != null) {
            for (String subjectId : teacher.getTeacherSubjects()) {
                Subjects subjects = subjectInterface.findSubjectById(Long.parseLong(subjectId));
                subjects.setTeacher_assigned(teacher1);
                subjectInterface.saveSubject(subjects);
            }
        }
        if (edited > 0){
            return "redirect:/view_staff_profile?staff=" + teacher.getTeacher_id();
        }
        return "redirect:/staffs";
    }

    @RequestMapping(value = "/edit_staff_profile")
    public String editStaffProfile(@RequestParam("staff") Long teacherId,RedirectAttributes attributes){
        Teacher teacher = teacherInterface.findTeacherByID(teacherId);
        attributes.addFlashAttribute("staff",teacher);
        return "redirect:/add_staff";
    }

    @RequestMapping(value = "/subject")
    public String subjects(Model model){
        DepartmentHead head = findAdmin();
        List<Subjects> subjectsList = new ArrayList<>();
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        for (Department department : departmentList){
            subjectsList.addAll(subjectInterface.subjectByDepartment(department,Student_Status.ACTIVE));
        }
        subjectsList.forEach(subjects -> subjects.getTeacher_assigned().setFullName
                (subjects.getTeacher_assigned().getStaffFullName()));
        model.addAttribute("subjects",subjectsList);
        return "view_subject";
    }

    @RequestMapping(value = "/add_subject")
    public String addSubject(Model model,@ModelAttribute("sub") Subjects subjects,
                             @RequestParam(value = "optSub",required = false) String value,
                             @RequestParam(value = "manSub",required = false) String manSub){
        DepartmentHead head = findAdmin();
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        List<Teacher> teacherList = teacherInterface.teacherByDepartment(departmentList.get(0),Student_Status.ACTIVE);
        teacherList.forEach(teacher -> teacher.setTeacherSubjects(subjectInterface.teacherSubjectName(teacher)));
        List<Teacher> possibleSubjectTeachers = new ArrayList<>();
        for (Teacher teacher : teacherList) {
            if (teacher.getTeacherSubjects().size() <= 1){
                teacher.setFullName(teacher.getStaffFullName());
                possibleSubjectTeachers.add(teacher);
            }
        }
        if (Objects.nonNull(value)){
            model.addAttribute("disable",value);
            model.addAttribute("dptId",subjects.getDepartment_subject().getDepartmentID());
            model.addAttribute("edit",1);
        } else if (Objects.nonNull(manSub)){
            model.addAttribute("edit",2);
        } else {
            model.addAttribute("edit",0);
        }
        model.addAttribute("sub", Objects.requireNonNullElseGet(subjects, Subjects::new));
        model.addAttribute("dept",departmentList);
        model.addAttribute("teachers",possibleSubjectTeachers);
        return "add_subject";
    }

    @RequestMapping(value = "/save_subject")
    public String saveSubject(@ModelAttribute("sub") Subjects subjects,
                              @RequestParam("edit") String edit,HttpServletRequest request){
        if (Objects.equals(edit, "1")){
            subjects.setOptions(subjects.getOptions());
            subjects.setDepartment_subject(departmentInterface.findByDepartmentId(
                    Long.parseLong(request.getParameter("deptId"))));
        } else if (Objects.equals(edit,"2")) {
            subjects.setOptions(subjects.getOptions());
            subjects.setDepartment_subject(departmentInterface.findByDepartmentId(
                    subjects.getDepartment_subject().getDepartmentID()));
        } else {
            subjects.setDepartment_subject(departmentInterface.findByDepartmentId(
                    subjects.getDepartment_subject().getDepartmentID()));
        }
        subjects.setOptions(SubjectOptions.MANDATORY);
        subjects.setSubject_Status(Student_Status.ACTIVE);
        subjects.setTeacher_assigned(teacherInterface.findTeacherByID(subjects.getTeacher_assigned().getTeacher_id()));
        subjectInterface.saveSubject(subjects);
        return "redirect:/subject";
    }

    @RequestMapping(value = "/add_opt_subject")
    public String addOptionalSubject(Model model){
        DepartmentHead head = findAdmin();
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        model.addAttribute("dept",departmentList);
        return "add_opt_sub";
    }

    @RequestMapping(value = "/sub_subjects")
    public String subSubject(@RequestParam("num") String num, Model model,
                            @RequestParam("dpt") Long id,@RequestParam("optName")String name){
        List<Teacher> teacherList = teacherInterface.teacherByDepartment(departmentInterface.findByDepartmentId(id),
                Student_Status.ACTIVE);
        Help.filterTeachers(teacherList,teacherList.get(0).getDepartment_Id(),subjectInterface);
        teacherList.forEach(teacher -> teacher.setFullName(teacher.getStaffFullName()));
        SubjectSet set = new SubjectSet();
        List<Subjects> subjectsList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(num); i++) {
            subjectsList.add(new Subjects());
        }
        set.setSubSubjectList(subjectsList);
        model.addAttribute("dptId",id);
        model.addAttribute("optName",name);
        model.addAttribute("tch",teacherList);
        model.addAttribute("subList",set);
        return "add_opt_subs_subject";
    }

    @RequestMapping(value = "/save_opt_sub_subject")
    public String saveOptSubSubject(@ModelAttribute("subList") SubjectSet set,
                                   @RequestParam("optName")String name,@RequestParam("dptId") Long id){
        Department department = departmentInterface.findByDepartmentId(id);
        OptionalSubject subject = new OptionalSubject();
        for (Subjects subjects : set.getSubSubjectList()){
            subjects.setDepartment_subject(department);
            subjects.setOptions(SubjectOptions.OPTIONAL);
            subjects.setTeacher_assigned(teacherInterface.findTeacherByID(
                    subjects.getTeacher_assigned().getTeacher_id()));
            subjects.setSubject_Status(Student_Status.ACTIVE);
        }
        subject.setDepartment_subject(department);
        subject.setOpt_sub_name(name);
        subject.setSubjectsSet(new HashSet<>(set.getSubSubjectList()));
        optionalSubjectInterface.saveOptionalSubject(subject);
        return "redirect:/subject";
    }

    @RequestMapping(value = "/edit_subject")
    public String editSubject(@RequestParam("sub") Long subjectId,RedirectAttributes attributes){
        Subjects subjects = subjectInterface.findSubjectById(subjectId);
        if(subjects.getOptions().equals(SubjectOptions.OPTIONAL)) {
            attributes.addFlashAttribute("sub", subjects);
            return "redirect:/add_subject?optSub=1";
        }else {
            attributes.addFlashAttribute("sub",subjects);
            return "redirect:/add_subject?manSub=1";
        }
    }

    // not yet completed
    @RequestMapping(value = "/delete_subject")
    public String deleteSubject(@RequestParam("sub") Long subjectId){
        Subjects subjects = subjectInterface.findSubjectById(subjectId);
        subjects.setTeacher_assigned(null);
        subjects.setSubject_Status(Student_Status.INACTIVE);
        if(subjects.getOptions().equals(SubjectOptions.OPTIONAL)){
            Set<Subjects> subSubject = new HashSet<>();
            OptionalSubject main = optionalSubjectInterface.findMainOptSubject(subjects.getSubject_id());
            for (Subjects sub : main.getSubjectsSet()){
                if (! sub.equals(subjects)){
                    subSubject.add(sub);
                }
            }
            main.setSubjectsSet(subSubject);
            optionalSubjectInterface.saveOptionalSubject(main);
        }
        subjectInterface.saveSubject(subjects);
        return "redirect:/subject";
    }

    @RequestMapping(value = "/opt_student_form")
    public String optStudentForm(Model model){
        DepartmentHead head = findAdmin();
        List<Department> department = departmentInterface.findDepartmentByHead(head);
        List<OptionalSubject> subjects = new ArrayList<>();
        List<Classes> classesList = classesInterface.listClassByDepartmentHead(head);
        for (Department department1 : department){
            subjects.addAll(optionalSubjectInterface.findByDepartment(department1));
        }
        model.addAttribute("opt",subjects);
        model.addAttribute("cls", classesList);
        return "optional_subject_student_form";
    }

    @RequestMapping(value = "/opt_sub_students")
    public String optSubjectStudents(@RequestParam("cls") String classId,Model model,
                                     @RequestParam("sub") String subjectId) throws AppExceptions{
        Classes classes = classesInterface.findClassById(classId);
        OptionalSubject subject = optionalSubjectInterface.findOptSubjectById(Long.parseLong(subjectId)) ;
        if (!classes.getParentClass().getClass_department().equals(subject.getDepartment_subject()))
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.BAD_REQUEST,"SELECTED CLASS DOES NOT OFFER SELECTED SUBJECT"));
        List<Student> studentList = studentInterface.studentsByClass(Student_Status.ACTIVE,classes);
        List<OptionalSubjectStudents> optSubStuList = new ArrayList<>();
        for (Student student : studentList){
            student.setFullName(student.getStudentFullName());
            optSubStuList.add(new OptionalSubjectStudents(student));
        }
        List<Subjects> subjectsList = subjectInterface.subjectByOptionAndDepartment(SubjectOptions.OPTIONAL,
                classes.getParentClass().getClass_department());
        List<Long> subIds = new ArrayList<>();
        for (Subjects subjects : subjectsList){
            subIds.add(subjects.getSubject_id());
        }
        List<Subjects> optSubjectList = optionalSubjectInterface.optSubjectList(subIds);
        model.addAttribute("optSubStudent",new OptionalSubjectStudentSet(optSubStuList));
        model.addAttribute("optSubject",optSubjectList);
        return "opt_student";
    }

    @RequestMapping(value = "/save_opt_stu_subject")
    public String saveOptSubjectStudent(@ModelAttribute("optSubStudent") OptionalSubjectStudentSet
                                                    studentSet){
        List<Student> studentList = new ArrayList<>();
        List<Long > subjectIds = new ArrayList<>();
        for (OptionalSubjectStudents students : studentSet.getSubjectStudents()){
            if (!subjectIds.contains(students.getOptSubjectId())){
                subjectIds.add(students.getOptSubjectId());
            }
        }
        for (Long subjectId : subjectIds) {
            Subjects subjects = subjectInterface.findSubjectById(subjectId);
            for (OptionalSubjectStudents students : studentSet.getSubjectStudents()) {
                if (Objects.equals(subjects.getSubject_id(), students.getOptSubjectId())) {
                    studentList.add(studentInterface.findStudentById(students.getStudent().getStudent_id()));
                }
            }
            subjects.setStudentSet(new HashSet<>(studentList));
            subjectInterface.saveSubject(subjects);
        }
        return "redirect:/subject";
    }

    @RequestMapping(value = "/students")
    public String students(Model model){
        DepartmentHead head = findAdmin();
        List<Classes> classesList = classesInterface.listClassByDepartmentHead(head);
        model.addAttribute("cls", classesList);
        return "student_view_form";
    }

    @RequestMapping(value = "/add_student")
    public String addStudent(Model model,@RequestParam(value = "cls") String classId){
        Classes classes = classesInterface.findClassById(classId);
        Student student = new Student();
        student.setClasses(classes);
        model.addAttribute("student", student);
        model.addAttribute("male",Gender.MALE);
        model.addAttribute("female",Gender.FEMALE);
        return "add_student";
    }

    // student primary key or student id;
    @RequestMapping(value = "/guardian_form",method = RequestMethod.POST , produces = "application/json")
    public String guardianForm(@ModelAttribute("student") Student student,Model model,
                               @RequestParam(value = "img",required = false) MultipartFile file){
        String fileName = "";
        if (file != null) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            student.setPhoto_name(fileName);
        }
        student.setClasses(classesInterface.findClassById(student.getClasses().getClass_id()));
        student.setStudent_status(Student_Status.ACTIVE);
        studentInterface.saveStudent(student);
        if (! fileName.equals("")){
            String upLoadDirectory = "./pictures/students_pics/"+student.getStudent_id();
            Help.saveFile(upLoadDirectory,fileName,file);
        }
        model.addAttribute("stu", student);
        model.addAttribute("guardian", new Guardian());
        model.addAttribute("relate",relationshipTypeInterface.findAllTypes());
        model.addAttribute("mal",Gender.MALE);
        model.addAttribute("fem",Gender.FEMALE);
        return "add_student_guardian";
    }

    @RequestMapping(value = "/edit_guardian_form",method = RequestMethod.POST , produces = "application/json")
    public String editedGuardianForm(@ModelAttribute("student") Student student,Model model,
                                     @RequestParam(value = "stuImg",required = false) MultipartFile file){
        String fileName = "";
        if (file != null) {
            fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            student.setPhoto_name(fileName);
        }

        student.setClasses(classesInterface.findClassById(student.getClasses().getClass_id()));
        student.setStudent_status(Student_Status.ACTIVE);
        studentInterface.saveStudent(student);
        if (! fileName.equals("")){
            String upLoadDirectory = "./pictures/students_pics/"+student.getStudent_id();
            Help.saveFile(upLoadDirectory,fileName,file);
        }
        Guardian guardian = guardianInterface.findByStudent(student);
        model.addAttribute("stu", student);
        model.addAttribute("guardian",Objects.requireNonNullElseGet(guardian, Guardian::new) );
        model.addAttribute("relate",relationshipTypeInterface.findAllTypes());
        model.addAttribute("mal",Gender.MALE);
        model.addAttribute("fem",Gender.FEMALE);
        return "edited_guardian_form";
    }

    @RequestMapping(value = "/save_guardian")
    public String saveGuardian(@ModelAttribute("guardian")Guardian guardian,
                               @RequestParam("student") String studentId){
        Student student = studentInterface.findStudentById(studentId);
        guardian.setRelationType(relationshipTypeInterface.findRelationById(
                guardian.getRelationType().getRelation_id()));
        guardian.setStudent(student);
        guardianInterface.saveGuardian(guardian);
        return "redirect:/admin_view_student?cls=" + student.getClasses().getClass_id();
    }

    @RequestMapping(value = "/add_configuration")
    public String sbaConfiguration(Model model,@ModelAttribute("con") SBAConfig config){
        DepartmentHead head = findAdmin();
        SBAConfig sbaConfig = new SBAConfig();
        sbaConfig.setAcademic_year(new Academic_Year());
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        model.addAttribute("dept",departmentList);
        model.addAttribute("config", (config == null) ? sbaConfig : config);
        model.addAttribute("year",(year == null) ? new Academic_Year() : year);
        return "sbaConfig";
    }

    @RequestMapping(value = "/sba_config_next")
    public String sbaConfigNext(@ModelAttribute("config") SBAConfig config,Model model) throws AppExceptions{
        config.setStatus(Status.CURRENT);
        Department department = departmentInterface.findByDepartmentId(config.getDepartment().getDepartmentID());
        config.setDepartment(department);
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        if (year == null){
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.BAD_REQUEST,"" +
                    "ACADEMIC YEAR SETTING MUST BE COMPLETED FIRST"));
        }
        config.setAcademic_year(year);
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfigByDepartmentAndStatus(department,Status.CURRENT);
        if (sbaConfig != null){
            sbaConfig.setStatus(Status.PASSED);
            sbaConfigInterface.saveSbaConfig(sbaConfig);
        }
        SBAConfig config1 = new SBAConfig();
        config1.setMaximum_class_work_mark(new ArrayList<>());
        for (int i = 0; i < config.getNumber_of_classwork_columns(); i++) {
            config1.getMaximum_class_work_mark().add(0);
        }
        sbaConfigInterface.saveSbaConfig(config);
        config1.setSba_config_id(sbaConfigInterface.findSBAConfig(department,year,Status.CURRENT).getSba_config_id());
        model.addAttribute("number",config1.getMaximum_class_work_mark());
        model.addAttribute("con",config1);
        return "sbaConfigNext";
    }

    @RequestMapping(value = "/save_config")
    public String saveConfig(@ModelAttribute("con") SBAConfig config){
        SBAConfig sbaConfig = sbaConfigInterface.findConfigById(config.getSba_config_id());
        sbaConfig.setMaximum_class_work_mark(config.getMaximum_class_work_mark());
        sbaConfigInterface.saveSbaConfig(sbaConfig);
        return "redirect:/configuration";
    }

    @RequestMapping(value = "/configuration")
    public String viewConfiguration(Model model) {
        List<SBAConfig> configs = sbaConfigInterface.findAllConfig();
        for (SBAConfig sbaConfig : configs) {
            List<SBA> sbaList = sbaInterface.findAllByConfig(sbaConfig);
            sbaConfig.setEditable(sbaConfig.getStatus().equals(Status.CURRENT) && sbaList.isEmpty());
        }
        model.addAttribute("config", configs);
        return "view_sba_config";
    }

    @RequestMapping(value = "/edit_config")
    public String editConfig(@RequestParam("con") Long configId,RedirectAttributes attributes){
        SBAConfig config = sbaConfigInterface.findConfigById(configId);
        attributes.addFlashAttribute("con",config);
        return "redirect:/add_configuration";
    }

    @RequestMapping(value = "/view_config")
    public String viewConfig(@RequestParam("con") Long configId,Model model){
        SBAConfig sbaConfig = sbaConfigInterface.findConfigById(configId);
        List<SBA> sbaList = sbaInterface.findAllByConfig(sbaConfig);
        sbaConfig.setEditable(!sbaConfig.getStatus().equals(Status.PASSED) && sbaList == null);
        model.addAttribute("config",sbaConfig);
        return "sba_config_profile";
    }

    @RequestMapping(value = "/batch_student")
    public String batchInsertStudent(Model model){
        DepartmentHead head = findAdmin();
        List<Classes> classesList = classesInterface.listClassByDepartmentHead(head);
        model.addAttribute("cls",classesList);
        return "add_batch_student";
    }

    @RequestMapping(value = "/save_batch_students")
    public String saveBatchStudent(@ModelAttribute("file")MultipartFile file,
                                   @RequestParam("cls")String classId) throws AppExceptions {
        Help.readFile(file,studentInterface,classesInterface,classId);
        return "redirect:/admin_view_student?cls="+classId;
    }
    @RequestMapping(value = "/promotion")
    public String promotion(@RequestParam("criteria") String criteria, Model model,
                            HttpServletRequest request,RedirectAttributes attributes) throws AppExceptions{
        DepartmentHead head = findAdmin();
        List<Classes> classWithNoEntry = new ArrayList<>();
        List<Classes> classWithUncompletedData = new ArrayList<>();
        List<Classes> classesList;
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        List<Subjects> subjectsList;
        List<ExamsScore> examsScoreList;
        int subjectNumber = 0, totalKey = 0;
        final List<OptionalSubject> list = new ArrayList<>(); ;
        Term term = termInterface.findTermById(3L);
        final Map<Subjects,Integer > optSubject = new HashMap<>();
        for (Department department : departmentList) {
            classesList = classesInterface.findClassByDepartment(department);
            list.addAll(optionalSubjectInterface.findByDepartment(department));
            subjectsList = subjectInterface.subjectByDepartment(department,Student_Status.ACTIVE);
            subjectsList.forEach(subjects -> {if
            (subjects.getOptions().equals(SubjectOptions.OPTIONAL)){
                list.forEach(subject -> {
                    if (subject.getSubjectsSet().contains(subjects)){
                       if (optSubject.containsKey(subjects)){
                           optSubject.put(subjects,optSubject.get(subjects)+1);
                       }else{
                           optSubject.put(subjects,1);
                       }
                    }
                });
            }
            });
            Set<Map.Entry<Subjects,Integer>> entries = optSubject.entrySet();
            for (Classes classes : classesList) {
                examsScoreList = examsScoreInterface.findExamsScoreByClassAndTerm(
                        classes, term);
                if (examsScoreList.isEmpty()) {
                    classWithNoEntry.add(classes);
                }
                for (Map.Entry<Subjects,Integer> entry : entries){
                    subjectNumber = (subjectsList.size() - entry.getValue()) + ++totalKey;
                }
                if (examsScoreList.size() != (subjectNumber)*studentInterface.studentNumberByClass(classes)){
                    classWithUncompletedData.add(classes);
                }
            }
        }
        if (!classWithNoEntry.isEmpty()){
            attributes.addFlashAttribute("classWithNOEntry",classWithNoEntry);
            return "redirect:/no_entry_found";
        } else if (!classWithUncompletedData.isEmpty()) {
            attributes.addFlashAttribute("uncompletedEntry",classWithUncompletedData);
            return "redirect:/uncompleted_entry_found";
        }
        int passMark = 0, trialMark = 0;
        if(Integer.parseInt(criteria) == 1 || Integer.parseInt(criteria) == 2){
            passMark = Integer.parseInt(request.getParameter("pass"));
            trialMark = Integer.parseInt(request.getParameter("trial"));
        }
        List<PromotionResults> results = Help.promotion(classesInterface,passMark,trialMark,Integer.parseInt(criteria),
                studentInterface,examsScoreInterface,sbaInterface,reportDetailInterface,
                head,termInterface,parentClassInterface);
        if (criteria.equals("3")){
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,"PROMOTION SUCCESSFUL"));
        }
        Help.recordInput(examsScoreInterface,sbaInterface,sbaRecordInterface,recordExamsInterface);
        Academic_Year currentYear = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        Academic_Year nextYear = academicYearInterface.findAcademicYearByMaxId(Status.NEXT);
        currentYear.setStatus(Status.PASSED);
        nextYear.setStatus(Status.CURRENT);
        academicYearInterface.saveAcademicYear(currentYear);
        academicYearInterface.saveAcademicYear(nextYear);
        model.addAttribute("results", results);
        return "promotion_results";
    }

    @RequestMapping(value = "/head_prev_pass_report")
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

    @RequestMapping(value = "/head_next_pass_report")
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
    private void passReportNextPrev(Report report,Model model,Integer index){
        Department department = report.getReportDetails().getReportDetailsId().getClass_id().getParentClass().getClass_department();
        Academic_Year year = report.getReportDetails().getAcademic_year();
        Term term = report.getReportDetails().getReportDetailsId().getTerm_id();
        Classes classes = report.getReportDetails().getReportDetailsId().getClass_id();
        CommonReportDetails details = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
        SBAConfig config = sbaConfigInterface.findSBAConfigByDepartmentAndYear(department,year);
        setModelForPassRecord(details,model,report,config,index);
    }

    @RequestMapping(value = "/parentClass")
    public String parentClass(Model model){
        DepartmentHead head = findAdmin();
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        List<ParentClass> parentClasses = new ArrayList<>();
        for (Department department : departmentList){
            parentClasses.addAll(parentClassInterface.findByDepartment(department));
        }
        model.addAttribute("parentClass",parentClasses);
        return "parentClass";
    }

    @RequestMapping(value = "/view_parent_class")
    public String viewParentClass(@RequestParam("parent") Long parentClassId, Model model){
        ParentClass parentClass = parentClassInterface.findParentClassById(parentClassId);
        List<Classes> classesList = classesInterface.classByParentClass(parentClass);
        model.addAttribute("parentClass", parentClass);
        model.addAttribute("classes",classesList);
        return "view_parent_class";
    }

    // throw error if parent class has only one subclass
    @RequestMapping(value = "/merge_parent_class")
    public String mergeParentClass(@RequestParam("parent") Long parentClassId) throws AppExceptions{
        ParentClass parentClass = parentClassInterface.findParentClassById(parentClassId);
        List<Classes> classesList = classesInterface.classByParentClass(parentClass);
        List<Student> studentList = new ArrayList<>();
        if (classesList.size() == 1)
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.BAD_REQUEST,
                    "CANNOT MERGE A PARENT CLASS WITH ONLY A SINGLE SUBCLASS"));
        else {
            Classes classes = classesList.get(0);
            for (Classes classes1 : classesList){
                studentList.addAll(studentInterface.studentsByClass(Student_Status.ACTIVE,classes1));
                classesInterface.deleteClass(classes1);
            }
            classes.setClass_name(parentClass.getParentClassName());
            classesInterface.saveClass(classes);
            Classes newClass = classesInterface.findClassById(classes.getClass_id());
            for (Student student : studentList){
                student.setClasses(newClass);
                studentInterface.saveStudent(student);
            }
        }
        return "redirect:/classes";
    }

    @RequestMapping(value = "/split_parent_class")
    public String splitClass(@RequestParam("parent") Long parentClassId,Model model){
        model.addAttribute("parent",parentClassId);
        return "split_class";
    }

    @RequestMapping(value = "/class_split")
    public String classSplit(@RequestParam("parentId") Long parentId,
                             @RequestParam("splitNum")String splitNum){
        ParentClass parentClass = parentClassInterface.findParentClassById(parentId);
        List<Classes> classesList = classesInterface.classByParentClass(parentClass);
        List<Student> studentList = new ArrayList<>();
        List<Teacher> teacherList = new ArrayList<>();
        List<Teacher> classTeacher = new ArrayList<>();
        List<Classes> newClassList = new ArrayList<>();
        List<Teacher> noClassTeacher = Help.noClassTeacher(classesInterface,parentClass.getClass_department()
            ,teacherInterface);
        Random random = new Random();
        for (Classes classes : classesList){
            studentList.addAll(studentInterface.studentsByClass(Student_Status.ACTIVE,classes));
            teacherList.addAll(classes.getClass_teachers());
            classTeacher.add(classes.getClass_teacher_id());
            classesInterface.deleteClass(classes);
        }
        int split = Integer.parseInt(splitNum);
        for (int i = 0; i < split; i++) {
            Classes newClass = new Classes();
            newClass.setClass_name(parentClass.getParentClassName() + " "+ Character.toString(65 + i));
            newClass.setParentClass(parentClass);
            newClass.setClass_teachers(new HashSet<>(teacherList));
            if (i < classTeacher.size()){
                newClass.setClass_teacher_id(classTeacher.get(i));
            } else {
                newClass.setClass_teacher_id(noClassTeacher.get(random.nextInt(noClassTeacher.size())));
            }
            newClassList.add(newClass);
        }
        for (Student student : studentList){
            student.setClasses(newClassList.get(random.nextInt(newClassList.size())));
            studentInterface.saveStudent(student);
        }
        return "redirect:/classes";
    }

    @RequestMapping(value = "/uncompleted_entry_found")
    public String uncompletedEntry(@ModelAttribute("uncompletedEntry") List<Classes> classes,
    Model model){
        for (Classes classes1 : classes){
            classes1.getClass_teacher_id().setFullName(classes1.getClass_teacher_id().getStaffFullName());
        }
        model.addAttribute("cls",classes);
        return "uncompleted";
    }

    @RequestMapping(value = "/no_entry_found")
    public String noEntry(@ModelAttribute("classWithNOEntry") List<Classes> classesList,Model model){
        for (Classes classes1 : classesList){
            classes1.getClass_teacher_id().setFullName(classes1.getClass_teacher_id().getStaffFullName());
        }
        model.addAttribute("cls",classesList);
        return "noEntry";
    }

    @RequestMapping(value = "/delete_student_verify")
    public String deleteStudentVerification(@RequestParam("stu") Long stuId, Model model){
        model.addAttribute("stuId",stuId);
        return "deleteVerification";
    }

    @RequestMapping(value = "/delete_staff_verification")
    public String deleteStaffVerification(@RequestParam("staff") Long teachId, Model model){
        model.addAttribute("staffId",teachId);
        return "deleteStaffVerification";
    }

    @RequestMapping(value = "/delete_subject_verify")
    public String deleteSubjectVerify(@RequestParam("sub") Long subId, Model model){
        model.addAttribute("subId",subId);
        return "deleteSubjectVerification";
    }

    @RequestMapping(value = "/opt_main_sub")
    public String optMainSubject(Model model){
        DepartmentHead head = findAdmin();
        List<OptionalSubject> subjects = new ArrayList<>();
        List<Department> departmentList = departmentInterface.findDepartmentByHead(head);
        for (Department department : departmentList){
            subjects.addAll(optionalSubjectInterface.findByDepartment(department));
        }
        model.addAttribute("subjects",subjects);
        return "opt_main_subject";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
}
