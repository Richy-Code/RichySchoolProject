package com.example.demo.enum_converter;

import com.example.demo.enum_entities.Student_Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StudentStatusConverter implements AttributeConverter<Student_Status, String> {
    @Override
    public String convertToDatabaseColumn(Student_Status student_status) {
        return student_status.getShortName();
    }

    @Override
    public Student_Status convertToEntityAttribute(String s) {
        return Student_Status.fromShortName(s);
    }
}
