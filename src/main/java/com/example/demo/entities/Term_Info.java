package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "term_info")
public class Term_Info {
    @Id
    @SequenceGenerator(
            name = "termInfo_sequence",
            sequenceName = "termInfo_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "termInfo_sequence"
    )
    private Long term_info_id;
    @Column(
            name = "open_date",
            nullable = false
    )
    String open_date;
    @Column(
            name = "closing_date",
            nullable = false
    )
    String closing_date;

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "academic_year",
            referencedColumnName = "academic_year_id"
    )
    private Academic_Year academic_year;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "term",
            referencedColumnName = "term_id"
    )
    private Term term;

    public Term_Info(String open_date,String closing_date, Academic_Year academic_year, Term term) {
        this.open_date = open_date;
        this.closing_date = closing_date;
        this.academic_year = academic_year;
        this.term = term;
    }
}
