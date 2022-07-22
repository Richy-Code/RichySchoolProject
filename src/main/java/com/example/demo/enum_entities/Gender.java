package com.example.demo.enum_entities;

public enum Gender {

    MALE("M"), FEMALE("F");

    private final String shortName;

    Gender(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }


    public static Gender fromShortName(String shortName){
        switch (shortName){
            case "M" ->{
                return Gender.MALE;
            }
            case "F" ->{
                return Gender.FEMALE;
            }
            default -> {
                throw new IllegalArgumentException("ShortName ["+ shortName + "] not supported.");
            }
        }
    }
}
