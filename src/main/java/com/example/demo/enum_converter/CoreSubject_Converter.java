package com.example.demo.enum_converter;

import com.example.demo.enum_entities.CoreSubject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CoreSubject_Converter implements AttributeConverter<CoreSubject,String> {

    @Override
    public String convertToDatabaseColumn(CoreSubject coreSubject) {
        return coreSubject.getShortName();
    }


    @Override
    public CoreSubject convertToEntityAttribute(String s) {
        return CoreSubject.fromShortName(s);
    }
}
