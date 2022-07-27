package com.example.demo.binding_entities;

import com.example.demo.entities.Classes;
import com.example.demo.entities.Term;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DummyClass {
    private Classes classes;
    private Term term;
}
