package com.example.demo.enum_entities;


public enum Status {
    CURRENT("C"), PASSED("P"),NEXT("N");

    private  String shortName;

    Status(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }


    public static Status fromShortName(String shortName){
        switch (shortName){
            case "C" ->{
                return Status.CURRENT;
            }
            case "P" ->{
                return Status.PASSED;
            }
            case "N" ->{
                return Status.NEXT;
            }
            default -> throw new IllegalArgumentException("ShortName ["+ shortName + "] not supported.");
        }
    }
}
