package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Remarks {

    @Id
    @SequenceGenerator(
            name = "remarks_sequence",
            sequenceName = "remarks_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "remarks_sequence"
    )
    private Long interest_id;
    private String conduct;

    public Remarks(String conduct) {
        this.conduct = conduct;
    }
}
