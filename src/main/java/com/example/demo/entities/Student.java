package com.example.demo.entities;

import com.example.demo.enum_converter.Gender_Converter;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue(generator = "stu-generator")
    @GenericGenerator(name = "stu-generator",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "student"),
            strategy = "com.example.demo.entities.StudentKeyGenerator")
    private String student_id;
    @Column(nullable = false)
    private String first_name;
    private String middle_name = " ";
    @Column(nullable = false)
    private String last_name;
    @Column(nullable = false)
    @Convert(converter = Gender_Converter.class)
    private Gender gender;
    @Column(nullable = false)
    private Student_Status student_status;
    private String photo_name;

    @Transient
    private String fullName;


    public Student( String first_name, String middle_name, String last_name,
                   Classes classes, Gender gender,Student_Status student_status) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.classes = classes;
        this.gender = gender;
        this.student_status = student_status;
    }
    public Student( String first_name, String middle_name, String last_name,
                   Classes classes, Gender gender,Student_Status student_status,String photo_name) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.classes = classes;
        this.gender = gender;
        this.photo_name = photo_name;
        this.student_status = student_status;
    }

    public Student(String first_name, String last_name,
                   Classes classes, Gender gender,Student_Status student_status ,String photo_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.classes = classes;
        this.gender = gender;
        this.photo_name = photo_name;
        this.student_status = student_status;
    }

    public Student(String first_name, String last_name,
                   Classes classes, Gender gender,Student_Status student_status) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.classes = classes;
        this.gender = gender;
        this.student_status = student_status;
    }

    @ManyToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "student_class_id",
            referencedColumnName = "class_id"
    )
    private Classes classes;


    @Transient
    public String getPhotoImage(){
        if ((photo_name == null || photo_name.equals("")) && (gender.equals(Gender.FEMALE))){
            return "/pictures/students_pics/male.png";
        } else if ((photo_name == null || photo_name.equals("")) && (gender.equals(Gender.MALE))) {
            return "/pictures/students_pics/female.png";
        }
        else
         return  "/pictures/students_pics/"+student_id+"/"+photo_name;
    }

    @Transient
    public String getStudentFullName(){
        if (middle_name == null || middle_name.equals(""))
            return first_name + " " + last_name;
        else
            return first_name + " " + middle_name + " " + last_name;
    }
}
