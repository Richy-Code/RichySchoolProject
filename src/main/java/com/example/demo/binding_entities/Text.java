package com.example.demo.binding_entities;

import java.util.Comparator;
import java.util.List;

public class Text {

    public static void main(String[] args) {
        List<Integer> integers = new java.util.ArrayList<>(List.of(1, 10, 4, 8, 3, 5, 2));
        integers.sort(Comparator.comparingInt(value -> value));
        integers.forEach(System.out::println);
    }

    public String shotSubjectName(String subjectName){
        String [] subsNames = subjectName.split(" ");
        if (subsNames.length == 1)
            return subjectName;
        else if (subsNames.length == 2){
            return subsNames[0].substring(0,3) + "." +" "+ subsNames[1];
        }else{
            StringBuilder subName = new StringBuilder();
            for (String subs : subsNames){
                int i = 0;
                if (subs.equalsIgnoreCase("and"))
                    continue;
                subName.append(subs.charAt(i++));
            }
            return subName.toString();
        }
    }
}
