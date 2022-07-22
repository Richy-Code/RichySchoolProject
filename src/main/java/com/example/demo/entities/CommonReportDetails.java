package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CommonReportDetails {
    @Id
    @SequenceGenerator(
            name = "common_sequence",
            sequenceName = "common_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "common_sequence"
    )
    private Long common_Details_id;
    private String headSignature = "";
    private String classTeacherSignature = "";
    private String next_term_begins = "";

    private int total_attendance;

    private int number_on_roll;
    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "academic_year",
            referencedColumnName = "academic_year_id"
    )
    private Academic_Year academic_year;
    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "classes",
            referencedColumnName = "class_id"
    )
    private Classes classes;

    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "term",
            referencedColumnName = "term_id"
    )

    private Term term;
    public CommonReportDetails(String headSignature, String classTeacherSignature,
                               String next_term_begins, Academic_Year academic_year,
                               Classes classes,Term term,int number_on_roll) {
        this.headSignature = headSignature;
        this.classTeacherSignature = classTeacherSignature;
        this.next_term_begins = next_term_begins;
        this.academic_year = academic_year;
        this.classes = classes;
        this.term = term;
        this.number_on_roll = number_on_roll;
    }

}

