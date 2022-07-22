package com.example.demo.entities;

import com.example.demo.enum_converter.Gender_Converter;
import com.example.demo.enum_entities.Gender;
import com.example.demo.enum_entities.Student_Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "contact_unique",
        columnNames = "contact"
))
public class Teacher{

    public Teacher(String first_name, String middle_name, String last_name,
                   String contact, Department department_Id,  Users users,Gender gender,Student_Status status) {
        this.first_name = first_name;
        this.middle_name = middle_name;
        this.last_name = last_name;
        this.contact = contact;
        this.department_Id = department_Id;
        this.users = users;
        this.gender = gender;
        this.status = status;
    }

    public Teacher(String first_name, String last_name, String contact, Department department_Id,
                   Users users,Gender gender,Student_Status status) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.contact = contact;
        this.department_Id = department_Id;
        this.users = users;
        this.gender = gender;
        this.status = status;
    }


    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long teacher_id;
    @Column(nullable = false)
    private String first_name;
    private String middle_name;
    @Column(nullable = false)
    private String last_name;
    @Column(
            nullable = false,
            length = 10
    )
    private String contact;

    @Column(nullable = false)
    @Convert(converter = Gender_Converter.class)
    private Gender gender;
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "department_Id",
            referencedColumnName = "departmentID"
    )
    private Department department_Id;

    public Teacher(Long teacher_id, String fullName) {
        this.teacher_id = teacher_id;
        this.fullName = fullName;
    }

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            }
    )
    @JoinColumn(
            name = "users",
            referencedColumnName = "user_id"
    )
    private Users users;

    @Column(nullable = false)
    private Student_Status status;

    @Column
    private String photo;
    @Transient
    private String fullName;

    @Transient
    private List<String> teacherClasses;

    @Transient
    private List<String> teacherSubjects;

    @Transient
    private String selected;

    @Transient
    private Classes class_teacher;
    @Transient
    public String getPhotoImage(){
        if (photo == null || photo.equals(""))
            return null;
        else
            return "/pictures/staffs_pics/"+teacher_id+"/"+photo;
    }

    @Transient
    public String getStaffFullName(){
        if (middle_name == null || middle_name.equals(""))
            return first_name + " " + last_name;
        else
            return first_name + " " + middle_name + " " + last_name;
    }
}
