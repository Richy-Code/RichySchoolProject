package com.example.demo.entities;

import com.example.demo.enum_converter.Gender_Converter;
import com.example.demo.enum_entities.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DepartmentHead {
    @Id
    @SequenceGenerator(
            name = "head_sequence",
            sequenceName = "head_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "head_sequence"
    )
    private Long head_id;
    @Column(nullable = false)
    private String firstName;
    private String middleName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    @Convert(converter = Gender_Converter.class)
    private Gender gender;
    public DepartmentHead(String firstName, String middleName, String lastName,Gender gender,Users user) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.user = user;
    }

    public DepartmentHead(String firstName, String lastName, Gender gender,Users user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.user = user;
    }

    @OneToOne(
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "user",
            referencedColumnName = "user_id"
    )
    private Users user;

    @Transient
    public String getHeadFullName(){
        if (middleName == null || middleName.equals(""))
            return firstName + " " + lastName;
        else
            return firstName + " " + middleName + " " + lastName;
    }
}
