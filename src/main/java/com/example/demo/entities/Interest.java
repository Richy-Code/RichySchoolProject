package com.example.demo.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Interest {

    @Id
    @SequenceGenerator(
            name = "interest_sequence",
            sequenceName = "interest_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "interest_sequence"
    )
    private Long interest_id;
    private String conduct;

    public Interest(String conduct) {
        this.conduct = conduct;
    }
}

