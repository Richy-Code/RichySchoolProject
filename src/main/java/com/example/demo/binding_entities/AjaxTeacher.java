package com.example.demo.binding_entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AjaxTeacher implements Serializable {
    private Long teachId;
    private String fullName;
}
