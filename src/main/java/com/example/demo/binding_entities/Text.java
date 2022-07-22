package com.example.demo.binding_entities;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Text {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("Richy1");
        System.out.println(password);
    }
}
