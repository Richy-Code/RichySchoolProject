package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "relation_type")
public class RelationType {
    public RelationType(String relationship) {
        this.relationship = relationship;
    }

    @Id
    @SequenceGenerator(
            name = "relation_sequence",
            sequenceName = "relation_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "relation_sequence"
    )
    private Long relation_id;
    private String relationship;
}
