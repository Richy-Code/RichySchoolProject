package com.example.demo.enum_converter;

import com.example.demo.enum_entities.Status;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class Status_Converter implements AttributeConverter<Status,String> {

    @Override
    public String convertToDatabaseColumn(Status status) {
        return status.getShortName();
    }

    @Override
    public Status convertToEntityAttribute(String s) {
        return Status.fromShortName(s);
    }
}
