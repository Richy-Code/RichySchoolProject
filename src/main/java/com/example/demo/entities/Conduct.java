package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Conduct {

    @Id
    @SequenceGenerator(
            name = "conduct_sequence",
            sequenceName = "conduct_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "conduct_sequence"
    )
    private Long conduct_id;
    private String conduct;

    public Conduct(String conduct) {
        this.conduct = conduct;
    }
}
