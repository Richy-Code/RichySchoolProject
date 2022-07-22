package com.example.demo.enum_entities;


public enum Student_Status {

    COMPLETED("C"), ACTIVE("A") ,INACTIVE("I");
    private String shortName;

    Student_Status(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public static Student_Status fromShortName(String shortName){
        switch (shortName){
            case "C" -> {
                return Student_Status.COMPLETED;
            }
            case "A" ->{
                return Student_Status.ACTIVE;
            }
            case "I" ->{
                return Student_Status.INACTIVE;
            }
            default -> {
                throw new IllegalArgumentException("ShortName ["+ shortName + "] not supported.");
            }
        }
    }
}
