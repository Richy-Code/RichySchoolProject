package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "terms",
        uniqueConstraints = @UniqueConstraint(
                name = "term_name_unique",
                columnNames = "term_name"
        )
)
public class Term {

    public Term(String term_name) {
        this.term_name = term_name;
    }

    @Id
    @SequenceGenerator(
            name = "term_sequence",
            sequenceName = "term_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "term_sequence"
    )
    @Column(name = "term_id")
    private Long term_id;

    @Column(
            name = "term_name",
            nullable = false
    )
    private String term_name;
}
