package com.example.demo.methods;

import com.example.demo.binding_entities.AjaxTeacher;
import com.example.demo.binding_entities.ErrorMessage;
import com.example.demo.binding_entities.ExamsEntry;
import com.example.demo.binding_entities.PromotionResults;
import com.example.demo.entities.*;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Status;
import com.example.demo.enum_entities.Student_Status;
import com.example.demo.error.AppExceptions;
import com.example.demo.error.NoPassRecordFoundException;
import com.example.demo.services.service_interface.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Help {
    public static String grade(double mark){
        String grade = "";
        if (mark >= 75)
            grade = "1";
        else if(mark >=70)
            grade = "2";
        else if(mark >= 65)
            grade = "3";
        else if(mark >= 60)
            grade = "4";
        else if(mark >= 55)
            grade = "5";
        else if(mark >= 50)
            grade = "6";
        else if (mark >= 45)
            grade = "7";
        else if(mark >= 40)
            grade = "8";
        else grade = "9";
        return grade;
    }

    public static void findPosition(List<ExamsEntry> examsEntryList){
        examsEntryList.sort(Comparator.comparing(examsEntry -> (examsEntry.getExamsScore()*0.5 +
                examsEntry.getTotal_sba())));
        int pos = 1;
        int index = examsEntryList.size()-1;
        int total1 = 0;
        Map<Double, Integer> map = new HashMap<>();
        for (ExamsEntry examsEntry : examsEntryList){
            double total = examsEntry.getExamsScore() * 0.5 + examsEntry.getTotal_sba();
            if (map.containsKey(total)){
                map.put(total, map.get(total)+1);
            }
            else {
                map.put(total,1);
            }
        }
       while (index >= 0){
           ExamsEntry examsEntry = examsEntryList.get(index);
           double total = examsEntry.getExamsScore() * 0.5 + examsEntry.getTotal_sba();
           total1 = total1 + map.get(total);
           for (int i = index; i >= examsEntryList.size()- total1; i--) {
               examsEntryList.get(i).setPosition(suffix(pos));
           }
           pos = pos + map.get(total);
           index = index - map.get(total);
        }
    }

   private static String suffix(int pos){
        String suffix = "";
        if(pos % 10 == 2)
            suffix = pos + "nd";
        else if( (pos % 10 == 1) && pos != 11)
            suffix = pos + "st";
        else if (pos % 10 == 3)
            suffix = pos + "rd";
        else
            suffix = pos + "th";
       return suffix;
   }

   public static String remarks(double total){
       String remark = "";
       if (total >= 75)
           remark = "HIGHEST";
       else if(total >=70)
           remark = "HIGHER";
       else if(total >= 65)
           remark = "HIGH";
       else if(total >= 60)
           remark = "HIGH AVERAGE";
       else if(total >= 55)
           remark = "AVERAGE";
       else if(total >= 50)
           remark = "LOW AVERAGE";
       else if (total >= 45)
           remark = "LOW";
       else if(total >= 40)
           remark = "LOWER";
       else remark = "FAIL";
       return remark;
   }

   public static void findPositionInClass(List<Report> reportList){
       reportList.sort(Comparator.comparing(Report::getTotal_score));
       int pos = 1;
       int index = reportList.size()-1;
       int total1 = 0;
       Map<Double, Integer> map = new HashMap<>();
       for (Report report : reportList){
           double total = report.getTotal_score();
           if (map.containsKey(total)){
               map.put(total, map.get(total)+1);
           }
           else {
               map.put(total,1);
           }
       }
       while (index >= 0){
           Report report = reportList.get(index);
           double total = report.getTotal_score();
           total1 = total1 + map.get(total);
           for (int i = index; i >= reportList.size()- total1; i--) {
               reportList.get(i).setPosition_in_class(suffix(pos));
           }
           pos = pos + map.get(total);
           index = index - map.get(total);
       }
   }

   public static void aggregateScore(List<Report> reportList){
        int aggregate = 0;
        List<Integer> best = new ArrayList<>();
        for (Report report : reportList){
            for (SubjectReportSummary summary : report.getSummaryList()){
                Optional<String> grade = Optional.ofNullable(summary.getGrade());
                if(summary.getSubject_name().equals("ENGLISH")||summary.getSubject_name().equals("MATHEMATICS") ||
                        summary.getSubject_name().equals("SOCIAL") || summary.getSubject_name().equals("SCIENCE")
                ){
                    aggregate += Integer.parseInt(grade.orElse("0"));
                }else {
                    //System.out.println("hello");
                    best.add(Integer.parseInt(grade.orElse("0")));
                }
            }
            best.sort(Comparator.comparingInt(value -> value));
            aggregate += best.get(best.size()-1) + best.get(best.size()-2);
            report.setAggregate(aggregate);
            best = new ArrayList<>();
            aggregate = 0;
        }
   }

   public static String signature(String firstName,String lastName){
       return firstName.charAt(0) + "." + lastName.charAt(0);
    }

    public static Term_Info termBegins(Term term, TermInfoInterface termInfoInterface,
                                       AcademicYearInterface academicYearInterface){
        List<Term_Info> term_infos;
        Term_Info term_info = null;
        if (term.getTerm_id()==1 || term.getTerm_id()==2){
            term_infos = termInfoInterface.findTermInfoByAcademicYear(
                    academicYearInterface.findAcademicYearByMaxId(Status.CURRENT));
            if (term.getTerm_id().intValue() == 1) {
                for (Term_Info termInfo : term_infos) {
                    if (termInfo.getTerm().getTerm_id() == 2) {
                        term_info = termInfo;
                    }
                }
            } else {
                for (Term_Info termInfo : term_infos) {
                    if (termInfo.getTerm().getTerm_id() == 3) {
                        term_info = termInfo;
                    }
                }
            }
        }else {
            term_infos = termInfoInterface.findTermInfoByAcademicYear(
                    academicYearInterface.findAcademicYearByMaxId(Status.NEXT));
            for (Term_Info termInfo : term_infos){
                if (termInfo.getTerm().getTerm_id()==1){
                    term_info = termInfo;
                }
            }
        }
        return (term_info != null) ? term_info : new Term_Info();
    }

    public static void saveRemark(String remark, RemarksInterface remarksInterface){
        Remarks teach = remarksInterface.findByRemark(remark);
        if (teach == null){
            remarksInterface.saveRemarks(new Remarks(remark));
        }
    }

    public static void saveConduct(String conduct, ConductInterface conductInterface){
        Conduct conduct1 = conductInterface.findByConduct(conduct);
        if (conduct1 == null){
            conductInterface.saveConduct(new Conduct(conduct));
        }
    }
    public static void saveInterest(String interest, InterestInterface interestInterface){
       Interest interest1 = interestInterface.findByInterest(interest);
       if (interest1 == null){
           interestInterface.saveInterest(new Interest(interest));
       }
    }

    public static String encryptedPassword(String password){

        return new BCryptPasswordEncoder().encode(password);
    }

    public static Users user(Teacher teacher,Set<Roles> rolesSet){
        Users users = new Users();
        users.setPlainPassword(passwordGenerator());
        users.setPassword(encryptedPassword(users.getPlainPassword()));
        users.setRoles(rolesSet);
        users.setUser_name(teacher.getFirst_name() + teacher.getTeacher_id());
        return users;
    }
    public static String passwordGenerator(){
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(122-49) + 49;
            while ((index >= 58 && index <=63)|| (index >= 91 && index <=96)){
                if (index <=63)
                    index = random.nextInt(8)+49;
                else
                    index = random.nextInt(25)+65;
            }
            password.append((char) index);
        }
        return password.toString();
    }

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)  {
        Path uploadPath = Paths.get(uploadDir);
        try {
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

        try(InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException es){
            System.out.println(es.getMessage());
        }

    }

    public static void readFile(MultipartFile file,StudentInterface studentInterface,
                                ClassesInterface classesInterface,String classId) throws AppExceptions {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line;
            String [] data;
            List<Student> studentList = new ArrayList<>();
            Classes classes = classesInterface.findClassById(classId);
            while ((line = reader.readLine()) !=null){
                Student student = new Student();
                data = line.split(" ");
                if (data.length == 4){
                    student.setFirst_name(data[0]);
                    student.setMiddle_name(data[1]);
                    student.setLast_name(data[2]);
                    if (data[3].equalsIgnoreCase("M"))
                        student.setGender(Gender.MALE);
                    else
                        student.setGender(Gender.FEMALE);
                }else {
                    student.setFirst_name(data[0]);
                    student.setMiddle_name(" ");
                    student.setLast_name(data[1]);
                    if (data[2].equalsIgnoreCase("M"))
                        student.setGender(Gender.MALE);
                    else
                        student.setGender(Gender.FEMALE);
                }
                student.setClasses(classes);
                student.setStudent_status(Student_Status.ACTIVE);
                studentList.add(student);
            }
            studentInterface.saveAllStudents(studentList);
        }catch (IOException e){
            throw new NoPassRecordFoundException
                    (new ErrorMessage(HttpStatus.CONFLICT,"COULD NOT READ FROM FILE"));
        }
    }

    public static List<PromotionResults> promotion(ClassesInterface classesInterface, int passMark, int trialMark, int choice,
                                                   StudentInterface studentInterface, ExamsScoreInterface scoreInterface,
                                                   SBAInterface sbaInterface, ReportDetailInterface detailInterface,
                                                   DepartmentHead head, TermInterface termInterface, ParentClassInterface
                                 parentClassInterface){
        List<Classes> classesList = classesInterface.listClassByDepartmentHead(head);
        List<Student> studentList = new ArrayList<>();
        List<PromotionResults> resultsList = new ArrayList<>();
        List<Term> termsList = termInterface.findAllTerm();
        List<ExamsScore> examsScoreList = new ArrayList<>();
        List<SBA> sbaList = new ArrayList<>();
        double total = 0,totalSba = 0, tempTotal=0;
        int passStudents = 0, failStudent = 0, onTrialStudent = 0;
        SBAConfig config;
        Term term3 = new Term();
        for (Term term : termsList){
            if (term.getTerm_id() == 3){
                term3 = term;
            }
        }
        for (Classes classes : classesList){
            if (classes.getClass_name().contains("jhs three")){
                continue;
            }
            Classes nextClass = nextClass(classes,parentClassInterface,classesInterface);
            studentList.addAll(studentInterface.studentsByClass(Student_Status.ACTIVE,classes));
            for (Student student : studentList){
                ReportDetails details = detailInterface.findDetailsById(new ReportDetailsId(student,
                        classes,term3));
                if (choice == 1){
                    examsScoreList = scoreInterface.scoreByStudentIdAndTerm(student, term3,classes.getParentClass().getClass_department());
                    sbaList = sbaInterface.sbaByStudentIdAndTerm(student,term3,classes.getParentClass().getClass_department());
                    for (ExamsScore score : examsScoreList){
                        total += score.getMarks();
                    }
                    total = total * ((100 - sbaList.get(0).getSbaConfig().getClasswork_scale())/100.0);
                    for (SBA sba : sbaList){
                        Optional<Double> totals = sba.getMarks().stream().reduce(Double::sum);
                        totalSba += totals.orElse(0.0);
                    }
                    totalSba = totalSba * (sbaList.get(0).getSbaConfig().getClasswork_scale()/100.0);
                    total = totalSba + total ;

                    if (total >= passMark){
                        details.setPromo_repeated_class("PROMOTED TO " + nextClass.getClass_name());
                        student.setClasses(nextClass);
                        passStudents++;
                    } else if (trialMark >0 && total >= trialMark) {
                        details.setPosition_in_class("ON TRIAL TO " + nextClass.getClass_name());
                        student.setClasses(nextClass);
                        onTrialStudent++;
                    }else {
                        details.setPromo_repeated_class("REPEATED IN "+ classes.getClass_name());
                        student.setClasses(classes);
                        failStudent++;
                    }
                }
                else if (choice == 2) {
                    for (Term term : termsList){
                        examsScoreList.addAll(scoreInterface.scoreByStudentIdAndTerm(student,term,classes.getParentClass().getClass_department()));
                        sbaList.addAll(sbaInterface.sbaByStudentIdAndTerm(student,term,classes.getParentClass().getClass_department()));
                        config = sbaList.get(0).getSbaConfig();
                        for (SBA sba : sbaList){
                            Optional<Double> totals = sba.getMarks().stream().reduce(Double::sum);
                            tempTotal += totals.orElse(0.0);
                        }
                        totalSba += tempTotal *(config.getClasswork_scale()/100.0);
                        tempTotal = 0;
                        for (ExamsScore score : examsScoreList){
                            tempTotal += score.getMarks();
                        }
                        total += tempTotal *((100 - config.getClasswork_scale())/100.0);
                        tempTotal = 0;
                        examsScoreList = new ArrayList<>();
                        sbaList = new ArrayList<>();
                    }
                    if (total >= passMark){
                        details.setPosition_in_class("PROMOTED TO " + nextClass.getClass_name());
                        student.setClasses(nextClass);
                        passStudents++;
                    } else if (trialMark >0 && total >= trialMark) {
                        details.setPosition_in_class("ON TRIAL TO " + nextClass.getClass_name());
                        student.setClasses(nextClass);
                        onTrialStudent++;
                    }else {
                        details.setPromo_repeated_class("REPEATED IN " + classes.getClass_name());
                        student.setClasses(classes);
                        failStudent++;
                    }
                }else{
                    details.setPromo_repeated_class("PROMOTED TO " + nextClass.getClass_name());
                    student.setClasses(nextClass);
                }
                detailInterface.saveDetails(details);
                studentInterface.saveStudent(student);
            }
            resultsList.add(new PromotionResults(classes,passStudents,onTrialStudent,failStudent));
            passStudents = 0;
            onTrialStudent = 0;
            failStudent = 0;
        }
        return resultsList;
    }

    private static Classes nextClass(Classes currentClass, ParentClassInterface parentClassInterface,
                              ClassesInterface classesInterface){
        Random random = new Random();
        List<ParentClass> parentClasses = parentClassInterface.findAllParentClass();
        ParentClass parentClass1 = new ParentClass();
        Classes next = new Classes();
        for (ParentClass parentClass : parentClasses){
            if (parentClass.getParentClassId() == currentClass.getParentClass().getParentClassId() +1){
                parentClass1 = parentClass;
            }
        }
        List<Classes> classesInNextParentClass = classesInterface.classByParentClass(parentClass1);
        List<Classes> classesInCurrentParentClass = classesInterface.classByParentClass
                (currentClass.getParentClass());
        if ((classesInCurrentParentClass.size() == 1) &&
            classesInNextParentClass.size() > 1){
            next = classesInNextParentClass.get(random.nextInt(classesInNextParentClass.size()));
        } else if ((classesInCurrentParentClass.size() > 1) && (classesInNextParentClass.size()==1)) {
            next = classesInNextParentClass.get(0);
        }else if((classesInCurrentParentClass.size() > 1) && classesInCurrentParentClass.size() ==
                classesInNextParentClass.size()) {
            String currentClassName = currentClass.getClass_name().substring(
                    currentClass.getClass_name().lastIndexOf(" "));
            String nextClassName;
            for(Classes classes : classesInNextParentClass){
                nextClassName = classes.getClass_name().substring(
                        classes.getClass_name().lastIndexOf(" "));
                if (nextClassName.equals(currentClassName)){
                    next = classes;
                }
            }

        }else if(classesInCurrentParentClass.size() == 1 && classesInNextParentClass.size()==1) {
            next = classesInNextParentClass.get(0);
        }else {
            next = classesInNextParentClass.get(random.nextInt(classesInNextParentClass.size()));
        }
        return next;
    }

    public static List<Teacher> noClassTeacher(ClassesInterface classesInterface,Department department,
                                               TeacherInterface teacherInterface){
        List<Classes> classesList = classesInterface.findClassByDepartment(department);
        List<Teacher> teacherList = teacherInterface.teacherByDepartment(department,Student_Status.ACTIVE);
        List<Teacher> results = new ArrayList<>();
        boolean found = false;
        for (Teacher teacher : teacherList){
            for (Classes classes : classesList){
                if (classes.getClass_teacher_id().equals(teacher)){
                    found = true;
                    break;
                }
            }
            if (!found)
                results.add(teacher);
        }
        return results;
    }

    public static void recordInput(ExamsScoreInterface examsScoreInterface,SBAInterface sbaInterface,
                                   SbaRecordInterface sbaRecordInterface,RecordExamsInterface examsInterface){
        List<ExamsScore> examsScores = examsScoreInterface.findAllScore();
        List<SBA> sbaLis = sbaInterface.findAllSba();
        List<Record_Exams> record_examsList = new ArrayList<>();
        List<Record_SBA> recordSbas = new ArrayList<>();
        for (ExamsScore score : examsScores){
            record_examsList.add(new Record_Exams(score.getExams_score_id(),score.getMarks(),
                    score.getPosition(), score.getAcademic_year().getAcademic_year_id()));
        }
        for (SBA sba : sbaLis){
            recordSbas.add(new Record_SBA(sba.getMarksId(),sba.getMarks(),
                    sba.getAcademic_year().getAcademic_year_id()));
            sbaRecordInterface.saveAll(recordSbas);
            examsInterface.saveAll(record_examsList);
        }
        examsScoreInterface.truncatexamscore();
        sbaInterface.truncateSba();
    }

    private static DepartmentHead head(){
        DepartmentHead departmentHead = new DepartmentHead();
        departmentHead.setGender(Gender.MALE);
        departmentHead.setFirstName("RICHY");
        departmentHead.setLastName("RICH");
        departmentHead.setMiddleName("RICH");
        departmentHead.setUser(users());
        return departmentHead;
    }

    private static Users users(){
        Set<Roles> rolesSet = new HashSet<>();
        rolesSet.add(new Roles("admin"));
        rolesSet.add(new Roles("user"));
        return new Users("Richy",new BCryptPasswordEncoder().encode("Richy1"),rolesSet);
    }

    public static List<Department> departmentList(){
        List<Department> departments = new ArrayList<>();
        departments.add(new Department("LOWER PRIMARY"));
        departments.add(new Department("UPPER PRIMARY"));
        departments.add(new Department("JHS",head()));
        return departments;
    }

    public static List<Term> termList(){
        List<Term> list = new ArrayList<>();
        list.add(new Term("TERM ONE"));
        list.add(new Term("TERM TWO"));
        list.add(new Term("TERM THREE"));
        return list;
    }
    public static List<ParentClass> parentClassList(List<Department> departmentList){
        List<ParentClass> list = new ArrayList<>();
        list.add(new ParentClass("BASIC ONE",departmentList.get(0)));
        list.add(new ParentClass("BASIC TWO",departmentList.get(0)));
        list.add(new ParentClass("BASIC THREE",departmentList.get(0)));
        list.add(new ParentClass("BASIC FOUR",departmentList.get(1)));
        list.add(new ParentClass("BASIC FIVE",departmentList.get(1)));
        list.add(new ParentClass("BASIC SIX",departmentList.get(1)));
        list.add(new ParentClass("JHS ONE",departmentList.get(2)));
        list.add(new ParentClass("JHS TWO",departmentList.get(2)));
        list.add(new ParentClass("JHS THREE",departmentList.get(2)));
        return list;
    }

    public static List<RelationType> relationTypes(){
        return List.of(new RelationType("PARENT"),new RelationType("FAMILY"),
                new RelationType("GUARDIAN"));
    }

    public static List<Teacher> filterTeachers(List<Teacher> teachers, Department department,
                                               SubjectInterface subjectInterface){
        List<Subjects> subjectsList = subjectInterface.subjectByDepartment(department,Student_Status.ACTIVE);
        Map<Teacher,Integer> map = new HashMap<>();
        for (Subjects subjects : subjectsList){
            if (! map.containsKey(subjects.getTeacher_assigned())){
                map.put(subjects.getTeacher_assigned(),1);
            }else {
                map.put(subjects.getTeacher_assigned(),map.get(subjects.getTeacher_assigned())+1);
            }
        }
        for (Map.Entry<Teacher, Integer> entries : map.entrySet()){
            if (entries.getValue() >= 2){
                teachers.remove(entries.getKey());
            }
        }
        return teachers;
    }
}
