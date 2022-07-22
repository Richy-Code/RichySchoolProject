package com.example.demo.entities;

import com.example.demo.enum_entities.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.function.Supplier;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SBAConfig {
    @Id
    @SequenceGenerator(
            name = "config_sequence",
            sequenceName = "config_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "config_sequence"
    )
    private Long sba_config_id;
    @Column(nullable = false)
    private int number_of_classwork_columns;
    @Column(nullable = false)
    @ElementCollection
    List<Integer> maximum_class_work_mark;
    @Column(nullable = false)
    private int classwork_scale;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private boolean editable;
    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "department_id",
            referencedColumnName = "departmentID"
    )
    private Department department;

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
    public SBAConfig(int number_of_classwork_columns,
                     List<Integer> maximum_class_work_mark, int classwork_scale,
                     Department department,Academic_Year year,Status status) {
        this.number_of_classwork_columns = number_of_classwork_columns;
        this.maximum_class_work_mark = maximum_class_work_mark;
        this.classwork_scale = classwork_scale;
        this.department = department;
        this.academic_year = year;
        this.status = status;
    }

    public SBAConfig(int number_of_classwork_columns, int classwork_scale,
                     Department department, Academic_Year academic_year,Status status) {
        this.number_of_classwork_columns = number_of_classwork_columns;
        this.classwork_scale = classwork_scale;
        this.department = department;
        this.academic_year = academic_year;
        this.status = status;
    }
}
