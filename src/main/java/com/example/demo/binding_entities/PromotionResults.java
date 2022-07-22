package com.example.demo.binding_entities;

import com.example.demo.entities.Classes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResults {
   private Classes classes;
   private int numberOfPassStudent;
   private int numberOfTrialStudent;
   private int numberOfFailStudent;
}
