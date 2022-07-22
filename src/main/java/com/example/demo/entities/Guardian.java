package com.example.demo.entities;

import com.example.demo.enum_converter.Gender_Converter;
import com.example.demo.enum_entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Guardian {
    @Id
    @SequenceGenerator(
            name = "guardian_sequence",
            sequenceName = "guardian_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "guardian_sequence"
    )
    private Long guardian_id;
    @Column
    private String guard_first_name;

    @Column
    private String guard_middle_name;

    @Column
    private String guard_last_name;

    @Column(length = 10)
    private String guard_contact;

    @Column(nullable = false)
    @Convert(converter = Gender_Converter.class)
    private Gender guard_gender;
    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            },
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "relation",
            referencedColumnName = "relation_id"
    )
    private RelationType relationType;

    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
            },
            fetch = FetchType.EAGER
    )
    @JoinColumn(
            name = "ward",
            referencedColumnName = "student_id"
    )
    private Student student;
    @Transient
    private String guard_fullName;

    @Transient
    public String getGuardianFullName(){
        if (guard_middle_name == null || guard_middle_name.equals(""))
            return guard_first_name + " " + guard_last_name;
        else
            return guard_first_name + " " + guard_middle_name + " " + guard_last_name;
    }
}
