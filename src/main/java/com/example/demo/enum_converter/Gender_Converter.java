package com.example.demo.enum_converter;

import com.example.demo.enum_entities.Gender;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//@Converter(autoApply = true)
public class Gender_Converter implements AttributeConverter<Gender , String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        return gender.getShortName();
    }

    @Override
    public Gender convertToEntityAttribute(String s) {
        return Gender.fromShortName(s);
    }
}
