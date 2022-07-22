package com.example.demo.binding_entities;

import com.example.demo.entities.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherSet {
    private List<Teacher> list;
}
