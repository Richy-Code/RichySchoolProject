package com.example.demo.controllers;

import com.example.demo.binding_entities.*;
import com.example.demo.entities.*;
import com.example.demo.enum_entities.*;
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

    private final YearCompletedInterface yearCompletedInterface;
    private final GuardianInterface guardianInterface;
    private final List<Report> reportList = new ArrayList<>();
    private final List<Report> passReportList = new ArrayList<>();

    private final List<CommonReportDetails> detailsList = new ArrayList<>();
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
                   YearCompletedInterface yearCompletedInterface,
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
        this.yearCompletedInterface = yearCompletedInterface;
        this.guardianInterface = guardianInterface;
    }
    @RequestMapping("/")
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
        reportList.clear();
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
                    summary.setSubject_name(subjects1.shotSubjectName());
                }else if (subjects1.getOptions().equals(SubjectOptions.OPTIONAL) && ! subjects1.getStudentSet().contains(student1)){
                    continue;
                }else {
                    summary.setSubject_name(subjects1.shotSubjectName());
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
        lab:for (Report report1 : reportList){
            for (ReportDetails details : reportDetails){
                if (report1.getStudent().equals(details.getReportDetailsId().getStudent_id())){
                    report1.setReportDetails(details);
                    continue lab;
                }
            }
            report1.setReportDetails(new ReportDetails());
        }
        Help.findPositionInClass(reportList);
        Help.aggregateScore(reportList,subjectInterface);
        report = reportList.get(0);
        System.out.println(report.getReportDetails().getHead_remarks());
        setModels(model,report,classes,term,0);
        return "head_filling_report";
    }

    private void setModels(Model model, Report report, Classes classes,Term term, Integer index){
        Academic_Year year = academicYearInterface.findAcademicYearByMaxId(Status.CURRENT);
        CommonReportDetails common = commonDetailsInterface.findDetailsByClass_Term_Year(year,classes,term);
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfig(classes.getParentClass().getClass_department(), year,Status.CURRENT);
        sbaConfig = (sbaConfig != null) ? sbaConfig : new SBAConfig();
        common = (common != null) ? common : new CommonReportDetails();
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
        model.addAttribute("num_on_roll",reportList.size());
    }

    @RequestMapping(value = "/save_head_filling_report")
    public String saveHeadFillingReport(@ModelAttribute("reportDetail")Report details,
                                        @RequestParam("cls")String classId,@RequestParam("trm") Long termId,
                                        @RequestParam("idx")Integer index,@RequestParam("stuId")String studId){
        Classes classes = classesInterface.findClassById(classId);
        Term term = termInterface.findTermById(termId);
        Student student = studentInterface.findStudentById(studId);
        ReportDetailsId detailsId = new ReportDetailsId(student,classes,term);
        reportDetailInterface.updateHeadRemarks(details.getReportDetails().getHead_remarks(),detailsId);
        reportList.get(index).setReportDetails(reportDetailInterface.findDetailsById(new ReportDetailsId(
                student,classes,term)));
        return "redirect:/printRecord?cls="+classes.getClass_id()+"&trm="+term.getTerm_id()+"&index="+index;
    }
    @RequestMapping(value = "/printRecord")
    public String printReport(@RequestParam("cls") String class_id,@RequestParam("trm")
    Long term_id, @RequestParam("index") Integer index,Model model){
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Report report = reportList.get(index);
        setModels(model,report,classes,term,index);
        return "printReport";
    }

    @RequestMapping(value = "/head_next_report")
    public String headNextReport(@RequestParam("cls") String class_id,@RequestParam("trm")
    Long term_id, @RequestParam("index") Integer index,Model model){
        Classes classes = classesInterface.findClassById(class_id);
        Term term = termInterface.findTermById(term_id);
        Report report = new Report() ;
        if (index > reportList.size()-1){
            return "redirect:/term_report_form";
        }else {
            report = reportList.get(++index);
        }
        setModels(model,report,classes,term,index);
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

    @RequestMapping(value = "/head_passed_record")
    public String passedRecord(@RequestParam("student_id") String student_id, Model model) throws AppExceptions {
        passReportList.clear();
        detailsList.clear();
        Student student = studentInterface.findStudentById(student_id);
        List<ExamsScore> scoreFromExams = examsScoreInterface.examsScoreByStudentIdFromExams(student,
                student.getClasses().getParentClass().getClass_department());;
        List<ExamsScore> scoreFromRecordTable = examsScoreInterface.examsScoreByStudentIdFromRecordExams(student,
                student.getClasses().getParentClass().getClass_department());
        List<SBA> sbaListFromRecordTable = sbaInterface.sbaByStudentIdFromRecordSBA(student,
                student.getClasses().getParentClass().getClass_department());
        List<SBA> sbaListFromSBA = sbaInterface.sbaByStudentIdFromSBA(student,
                student.getClasses().getParentClass().getClass_department());
        List<ReportDetails> details = reportDetailInterface.detailsByStudentId(student);
        Map<DummyClass,List<SubjectReportSummary>> listMap = new HashMap<>();
        SBAConfig sbaConfig = new SBAConfig();
        List<TempSubjectSummary> subjectSummaries = new ArrayList<>();
        if (scoreFromExams.isEmpty() && scoreFromRecordTable.isEmpty()){
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,
                    "NO RECORD FOUND"));
        }
        for (ExamsScore score : scoreFromExams){
            for (SBA sba : sbaListFromSBA){
                if (sba.getMarksId().equals(score.getExams_score_id())){
                    subjectSummaries.add(new TempSubjectSummary(score,sba));
                }
            }
        }
        for (ExamsScore score : scoreFromRecordTable){
            for (SBA sba : sbaListFromRecordTable){
                if (sba.getMarksId().equals(score.getExams_score_id())){
                    subjectSummaries.add(new TempSubjectSummary(score,sba));
                }
            }
        }
        for (TempSubjectSummary summary : subjectSummaries){
            sbaConfig = sbaConfigInterface.findSBAConfigByDepartmentAndYear(student.getClasses().getParentClass().getClass_department(),
                    summary.getExamsScore().getAcademic_year());
            detailsList.add(commonDetailsInterface.findDetailsByClass_Term_Year(summary.getExamsScore().getAcademic_year(),
                    summary.getExamsScore().getExams_score_id().getClass_id(), summary.getExamsScore().getExams_score_id().getTerm_id()));
            Optional<Double> total = summary.getClassScore().getMarks().stream().reduce(Double::sum);
            total = Optional.of(total.orElse(0.0)*(sbaConfig.getClasswork_scale()/100.0));
            double totalScore = total.orElse(0.0)+(summary.getExamsScore().getMarks() *
                    ((100 - sbaConfig.getClasswork_scale())/100.0));
            if (!listMap.containsKey(new DummyClass(summary.getExamsScore().getExams_score_id().getClass_id(),
                    summary.getExamsScore().getExams_score_id().getTerm_id()))){
                List<SubjectReportSummary> list = new ArrayList<>();
                list.add(new SubjectReportSummary(summary.getExamsScore().getExams_score_id().getSubject_id().shotSubjectName(),
                        (summary.getExamsScore().getMarks()*((100 - sbaConfig.getClasswork_scale())/100.0)),total.orElse(0.0),totalScore,summary.getExamsScore().getPosition(),
                        Help.remarks(totalScore),Help.grade(totalScore)));
                listMap.put(new DummyClass(summary.getExamsScore().getExams_score_id().getClass_id(),
                                summary.getExamsScore().getExams_score_id().getTerm_id()),
                        list);
            }else {
                List<SubjectReportSummary> list = listMap.get(new DummyClass(summary.getExamsScore().getExams_score_id().getClass_id(),
                        summary.getExamsScore().getExams_score_id().getTerm_id()));
                list.add(new SubjectReportSummary(summary.getExamsScore().getExams_score_id().getSubject_id().shotSubjectName(),
                        summary.getExamsScore().getMarks(),total.orElse(0.0),totalScore,summary.getExamsScore().getPosition(),
                        Help.remarks(totalScore),Help.grade(totalScore)));
            }
        }
        for (Map.Entry<DummyClass,List<SubjectReportSummary>> entry : listMap.entrySet()){
            double total = 0;
            int size = entry.getValue().size();
            ReportDetails reportDetail = new ReportDetails();
            reportDetail.setReportDetailsId(new ReportDetailsId(student,new Classes(),new Term()));
            for (SubjectReportSummary summary : entry.getValue()){
                total += summary.getTotal();

            }
            for (ReportDetails reportDetails : details){
                if (reportDetails.getReportDetailsId().getClass_id().equals(entry.getKey().getClasses())&&
                        reportDetails.getReportDetailsId().getTerm_id().equals(entry.getKey().getTerm())){
                    reportDetail = reportDetails;
                }
            }
            passReportList.add(new Report(student,student.getStudentFullName(),total,0,(total/size),
                    reportDetail,"",entry.getValue()));
        }
        Help.aggregateScore(passReportList,subjectInterface);
        setModelForPassRecord(detailsList.get(0),model,passReportList.get(0),sbaConfig,1);
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
        model.addAttribute("completed",Student_Status.COMPLETED);
        model.addAttribute("active",Student_Status.ACTIVE);
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
        List<Classes> classesList = classesInterface.findClassByDepartment(staff.getDepartment_Id());
        classesList.forEach(classes -> {
            if(classes.getClass_teacher_id().equals(staff)){
                classes.setClass_teacher_id(new Teacher());
            }
            classes.getClass_teachers().remove(staff);
            classesInterface.saveClass(classes);
        });
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
            passwordInterface.savePassword(new PlainPassword(
                    teacher.getUsers().getPlainPassword(),teacher.getUsers()));
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
                             @RequestParam(value = "manSub",required = false) String manSub)
    {
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
        model.addAttribute("core", CoreSubject.YES);
        model.addAttribute("notCore",CoreSubject.NOT);
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
            OptionalSubject main = optionalSubjectInterface.findMainOptSubject(subjects.getSubject_id());
            main.getSubjectsSet().remove(subjects);
            if (main.getSubjectsSet().size() == 1){
                for (Subjects subs : main.getSubjectsSet()) {
                    subs.setOptions(SubjectOptions.MANDATORY);
                    subjectInterface.saveSubject(subs);
                }
                optionalSubjectInterface.deleteOptionalSubjects(main);
            }else {
                optionalSubjectInterface.saveOptionalSubject(main);
            }
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

        model.addAttribute("optSubStudent",new OptionalSubjectStudentSet(optSubStuList));
        model.addAttribute("optSubject",subject.getSubjectsSet());
        return "opt_student";
    }

    @RequestMapping(value = "/save_opt_stu_subject")
    public String saveOptSubjectStudent(@ModelAttribute("optSubStudent") OptionalSubjectStudentSet
                                                    studentSet){
        List<Student> studentList = new ArrayList<>();
        Map<Subjects, List<Student>> map = new HashMap<>();
        for (OptionalSubjectStudents students : studentSet.getSubjectStudents()){
            Subjects subjects = subjectInterface.findSubjectById(students.getOptSubjectId());
            Student student = studentInterface.findStudentById(students.getStudent().getStudent_id());
            if (map.containsKey(subjects)) {
                studentList = map.get(subjects);
            }
            studentList.add(student);
            map.put(subjects,studentList);
            studentList = new ArrayList<>();
        }
        for (Map.Entry<Subjects,List<Student>> entry : map.entrySet()){
            entry.getKey().setStudentSet(new HashSet<>(entry.getValue()));
            subjectInterface.saveSubject(entry.getKey());
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
    public String batchInsertStudent(){
        return "batch";
    }

    @RequestMapping(value = "/batch_student_form")
    public String batchStudentForm(Model model){
        DepartmentHead head = findAdmin();
        List<Classes> classesList = classesInterface.listClassByDepartmentHead(head);
        model.addAttribute("cls",classesList);
        return "batch_student_form";
    }

    @RequestMapping(value = "/save_batch_students",method = RequestMethod.POST , produces = "application/json")
    public String saveBatchStudent(@ModelAttribute("file")MultipartFile file, Model model,
                                   @RequestParam("clsId")String classId) throws AppExceptions {

        List<Integer>  index =  Help.readFile(file,studentInterface,classId,relationshipTypeInterface, guardianInterface,classesInterface);
        if ( ! index.isEmpty()){
           model.addAttribute("reject",index);
           return "rejected_record";
        }
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
                head,termInterface,parentClassInterface,yearCompletedInterface,academicYearInterface);
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

    @RequestMapping(value = "/head_next_pass_report")
    public String next_pass_report(@RequestParam("index") Integer index,Model model) throws AppExceptions{
        Report report = new Report();
        if (index > passReportList.size()-1){
            String message = "END OF STUDENT RECORD";
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.OK,message));
        }else {
            report = passReportList.get(index);
        }
        SBAConfig sbaConfig = sbaConfigInterface.findSBAConfigByDepartmentAndYear(
                report.getStudent().getClasses().getParentClass().getClass_department(),
                report.getReportDetails().getAcademic_year());
        setModelForPassRecord(detailsList.get(index),model,report,sbaConfig,++index);
        return "pass_report";
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
        final List<String> classNames = new ArrayList<>();
        classesList.forEach(classes -> classNames.add(classes.getClass_name()));
        model.addAttribute("parentClass", parentClass);
        model.addAttribute("classes",classNames);
        return "view_parent_class";
    }

    /*
    throw error if parent class has only one subclass
    add field status to class;
     */
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
            for (int i = 1; i < classesList.size(); i++) {
                Classes classes1 = classesList.get(i);
                studentList.addAll(studentInterface.studentsByClass(Student_Status.ACTIVE,classes1));
                classes1.setParentClass(null);
                classes1.setClass_teachers(new HashSet<>());
                classes1.setClass_teacher_id(null);
                classesInterface.saveClass(classes1);
            }
            classes.setClass_name(parentClass.getParentClassName());
            classesInterface.saveClass(classes);
            Classes newClass = classesInterface.findClassById(classes.getClass_id());
            if (! studentList.isEmpty()) {
                for (Student student : studentList) {
                    student.setClasses(newClass);
                    studentInterface.saveStudent(student);
                }
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
            classes.setParentClass(null);
            classes.setClass_teacher_id(null);
            classes.setClass_teachers(new HashSet<>());
            classesInterface.saveClass(classes);
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
        newClassList.forEach(classesInterface::saveClass);
        if (! studentList.isEmpty()) {
            for (Student student : studentList) {
                student.setClasses(newClassList.get(random.nextInt(newClassList.size())));
                studentInterface.saveStudent(student);
            }
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
    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(value = "/view_opt_main")
    public String viewOptMainSub(@RequestParam("sub")Long subId,Model model){
        OptionalSubject subject = optionalSubjectInterface.findOptSubjectById(subId);
        model.addAttribute("subject",subject);
        return "opt_main_sub_view";
    }

    @RequestMapping(value = "/edit_opt_main")
    public String editOptMainSub(@RequestParam("sub") Long subId, Model model){
        OptionalSubject subject = optionalSubjectInterface.findOptSubjectById(subId);
        model.addAttribute("subject",subject);
        return "edit_opt_main_sub";
    }

    @RequestMapping(value = "/edit_sub_subjects")
    public String editSubSubject(@RequestParam("num") String number,Model model,
                                 @ModelAttribute("subject") OptionalSubject subjects){
        OptionalSubject optionalSubject = optionalSubjectInterface.findOptSubjectById(subjects.getOpt_sub_id());
        optionalSubject.setOpt_sub_name(subjects.getOpt_sub_name());
        optionalSubjectInterface.saveOptionalSubject(optionalSubject);
        List<Teacher> teacherList = teacherInterface.teacherByDepartment(
                optionalSubject.getDepartment_subject(), Student_Status.ACTIVE);
        Help.filterTeachers(teacherList,teacherList.get(0).getDepartment_Id(),subjectInterface);
        teacherList.forEach(teacher -> teacher.setFullName(teacher.getStaffFullName()));
        SubjectSet set = new SubjectSet();
        if ( Integer.parseInt(number) > optionalSubject.getSubjectsSet().size()){
            int limit = Integer.parseInt(number) - optionalSubject.getSubjectsSet().size();
            for (int i = 0; i <limit ; i++) {
                optionalSubject.getSubjectsSet().add(new Subjects());
            }
        }
        set.setSubSubjectList(new ArrayList<>(optionalSubject.getSubjectsSet()));
        model.addAttribute("numberOfSubject",Integer.parseInt(number));
        model.addAttribute("optSubject",optionalSubject);
        model.addAttribute("subList",set);
        model.addAttribute("tch",teacherList);
        return "edit_opt_main_sub_subject";
    }

    @RequestMapping(value = "/save_edited_opt_sub_subject")
    public String saveEditedOptMainSubject(@ModelAttribute("subList")SubjectSet set,
                                           @RequestParam("optId") Long optId){
        OptionalSubject optionalSubject = optionalSubjectInterface.findOptSubjectById(optId);
        Set<Subjects> subjectsSet = new HashSet<>();
        set.getSubSubjectList().forEach(subjects -> {
            Optional<Long> subId = Optional.ofNullable(subjects.getSubject_id());
            if (subId.isPresent() && subId.get() > 0){
                Subjects subject = subjectInterface.findSubjectById(subjects.getSubject_id());
                subject.setSubject_name(subjects.getSubject_name());
                subject.setTeacher_assigned(teacherInterface.findTeacherByID(
                        subjects.getTeacher_assigned().getTeacher_id()));
                subject.setSubject_Status(Student_Status.ACTIVE);
                subject.setDepartment_subject(optionalSubject.getDepartment_subject());
                subject.setOptions(SubjectOptions.OPTIONAL);
                subjectInterface.saveSubject(subject);
                subjectsSet.add(subject);
            }else {
                subjects.setSubject_Status(Student_Status.ACTIVE);
                subjects.setDepartment_subject(optionalSubject.getDepartment_subject());
                subjects.setOptions(SubjectOptions.OPTIONAL);
                subjects.setTeacher_assigned(teacherInterface.findTeacherByID(
                        subjects.getTeacher_assigned().getTeacher_id()));
                subjectsSet.add(subjects);
            }
        });
        optionalSubject.setSubjectsSet(subjectsSet);
        optionalSubjectInterface.saveOptionalSubject(optionalSubject);
        return "redirect:/subject";
    }

    @RequestMapping(value = "/delete_opt_verify")
    public String deleteOptMainSub(@RequestParam("sub") Long subId,Model model){
        OptionalSubject optionalSubject = optionalSubjectInterface.findOptSubjectById(subId);
        model.addAttribute("subs",optionalSubject);
        return "delete_opt_main_sub_verify";
    }

    @RequestMapping(value = "/delete_opt_main_sub")
    public String deleteOptMainSubAndSubSubject(@RequestParam("sub")Long subId){
        OptionalSubject optionalSubject = optionalSubjectInterface.findOptSubjectById(subId);
        for (Subjects subjects : optionalSubject.getSubjectsSet()){
            subjects.setSubject_Status(Student_Status.INACTIVE);
            subjectInterface.saveSubject(subjects);
        }
        optionalSubjectInterface.deleteOptionalSubjects(optionalSubject);
        return "redirect:/opt_main_sub";
    }

    @RequestMapping(value = "/admin_records_form")
    public String adminRecordForm(Model model) throws AppExceptions{
        List<Academic_Year> yearList = yearCompletedInterface.yearCompleted();
        if (yearList.isEmpty()){
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,
                    "NO PASSED STUDENT RECORD"));
        }
        model.addAttribute("years",yearList);
        return "admin_record_form";
    }

    @RequestMapping(value = "/admin_records")
    public String adminPassRecordsByStudent(@RequestParam("yrs") Long yearId,Model model){
        List<StudentCompletedYear> studentCompletedYears = yearCompletedInterface.studentByCompletedYear(
                academicYearInterface.findAcademicYearById(yearId));
        model.addAttribute("students",studentCompletedYears);
        return "passed_student_list";
    }

    @RequestMapping(value = "/promotion_form")
    public String promotionForm(){
        return "promotion";
    }


    @RequestMapping(value = "/head_summary_report")
    public String headSummaryReport(@RequestParam("student_id")String studentId,
                                    Model model) throws AppExceptions{
        Student student = studentInterface.findStudentById(studentId);
        List<SummaryReport> summaryReports = examsScoreInterface.summaryReport(studentId,
                student.getClasses().getParentClass().getClass_department());
        List<SummaryReport> summaryReportFromRecord = examsScoreInterface.summaryReportFromRecordTable(studentId,
                student.getClasses().getParentClass().getClass_department());
        if (summaryReports.size() == 0 && summaryReportFromRecord.size() == 0){
            throw new NoPassRecordFoundException(new ErrorMessage(HttpStatus.NOT_FOUND,
                    "NO RECORD FOUND"));
        }
        Map<String, Map<String,List<TermRecords>>> listMap = new HashMap<>();
        List<String> subjectName = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        List<TermRecords> seriesList = new ArrayList<>();
        for (SummaryReport report : summaryReports) {
            Map<String, List<TermRecords>> map;
            if (!listMap.containsKey(report.getSubjectName())) {
                map = new HashMap<>();
                map.put(report.getClassName(),List.of(new TermRecords(report.getTermName(),report.getMarks())));
            } else {
                map = listMap.get(report.getSubjectName());
                List<TermRecords> list = map.get(report.getClassName());
                list.add(new TermRecords(report.getTermName(),report.getMarks()));
                map.put(report.getClassName(),list);
            }
            listMap.put(report.getSubjectName(),map);
        }
        for (SummaryReport report : summaryReportFromRecord){
            Map<String, List<TermRecords>> map;
            if (!listMap.containsKey(report.getSubjectName())) {
                map = new HashMap<>();
                map.put(report.getClassName(),List.of(new TermRecords(report.getTermName(),report.getMarks())));
            } else {
                map = listMap.get(report.getSubjectName());
                List<TermRecords> list = map.get(report.getClassName());
                list.add(new TermRecords(report.getTermName(),report.getMarks()));
                map.put(report.getClassName(),list);
            }
            listMap.put(report.getSubjectName(),map);
        }
        for (Map.Entry<String,Map<String,List<TermRecords>>> entry : listMap.entrySet()){
            subjectName.add(entry.getKey());
            Map<String,List<TermRecords>> listMap1 = entry.getValue();
            for (Map.Entry<String,List<TermRecords>> listEntry : listMap1.entrySet()){
                List<Double> doubleList = new ArrayList<>();
                for (TermRecords report : listEntry.getValue()){
                    categories.add(report.getTerm());
                    doubleList.add(report.getMark());
                }
                seriesList.add(new TermRecords(listEntry.getKey(),doubleList));
            }
        }
        for (int i = 0; i < categories.size(); i++) {
            if (i > 2)
                categories.remove(categories.get(i));
        }
        System.out.println(seriesList);
        model.addAttribute("category",categories);
        model.addAttribute("series",seriesList);
        model.addAttribute("subject",subjectName);
        return "headSummaryReport";
    }

}
