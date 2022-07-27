package com.example.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PlainPassword {
    @Id
    @SequenceGenerator(
            name = "password_sequence",
            sequenceName = "password_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "password_sequence"
    )
    private Long passwordId;

    @Column(nullable = false)
    private String password;

    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id"
    )

    private Users users;

    public PlainPassword(String password, Users users) {
        this.password = password;
        this.users = users;
    }


}
