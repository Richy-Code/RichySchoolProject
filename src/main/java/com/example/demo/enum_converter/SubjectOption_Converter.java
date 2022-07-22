package com.example.demo.enum_converter;

import com.example.demo.enum_entities.SubjectOptions;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SubjectOption_Converter implements AttributeConverter<SubjectOptions,String> {

    @Override
    public String convertToDatabaseColumn(SubjectOptions subjectOptions) {
        return subjectOptions.getShortName();
    }

    @Override
    public SubjectOptions convertToEntityAttribute(String s) {
        return SubjectOptions.fromShortName(s);
    }
}
