package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SBA {
    @EmbeddedId
    private MarksId marksId;
    @ElementCollection(fetch = FetchType.EAGER)
    List<Double> marks = new ArrayList<>();
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "academic_year",
            referencedColumnName = "academic_year_id"
    )
    private Academic_Year academic_year;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "config_id",
            referencedColumnName = "sba_config_id"
    )
    private SBAConfig sbaConfig;
}
