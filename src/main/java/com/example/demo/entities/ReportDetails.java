package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report_details")
public class ReportDetails {
    @EmbeddedId
    private ReportDetailsId reportDetailsId;
    @Column(nullable = false)
    private int attendance;
    @Column(nullable = false)
    private String interest = " ";
    @Column(nullable = false)
    private String attitude = " ";
    @Column(nullable = false)
    private String head_remarks = " ";
    @Column(nullable = false)
    private String teacher_remark = " ";
    private String promo_repeated_class = " ";

    private String position_in_class = "";
    @ManyToOne(
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
}
