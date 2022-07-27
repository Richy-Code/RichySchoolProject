package com.example.demo.enum_entities;

public enum CoreSubject {
    YES("Y"),NOT("N");
    private String shortName;

    CoreSubject(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }


    public static CoreSubject fromShortName(String shortName){
        switch (shortName){
            case "Y" ->{
                return CoreSubject.YES;
            }
            case "N" ->{
                return CoreSubject.NOT;
            }
            default -> {
                throw new IllegalArgumentException("ShortName ["+ shortName + "] not supported.");
            }
        }
    }
}
