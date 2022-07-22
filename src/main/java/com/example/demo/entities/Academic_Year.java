package com.example.demo.entities;


import com.example.demo.enum_entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "current_academic_year_info")
public class Academic_Year {
    @Id
    @SequenceGenerator(
            name = "academic_sequence",
            sequenceName = "academic_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "academic_sequence"
    )
    private Long academic_year_id;
    @Column(nullable = false)
    private String first_year;

    @Column(nullable = false)
    private String last_year;
    @Column(nullable = false)
    private Status status;
    public Academic_Year(String first_year, String last_year,Status status) {
        this.first_year = first_year;
        this.status = status;
        this.last_year = last_year;
    }

}
