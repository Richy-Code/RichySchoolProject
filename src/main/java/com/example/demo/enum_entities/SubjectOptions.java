package com.example.demo.enum_entities;

public enum SubjectOptions {
    OPTIONAL("P"), MANDATORY("M");

    private String shortName;

    SubjectOptions(String shortName) {
        this.shortName = shortName;
    }
    public static SubjectOptions fromShortName(String shortName){
        switch (shortName){
            case "P" ->{
                return SubjectOptions.OPTIONAL;
            }
            case "M" ->{
                return SubjectOptions.MANDATORY;
            }
            default -> throw new IllegalArgumentException("ShortName ["+ shortName + "] not supported.");
        }
    }

    public String getShortName() {
        return shortName;
    }
}
