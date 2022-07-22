package com.example.demo.services.service_interface;

import com.example.demo.entities.Interest;

import java.util.List;

public interface InterestInterface {
    void saveInterest(Interest interest);
    List<Interest> findAllInterest();

    void saveAllInterest(List<Interest> interestList);

    Interest findByInterest(String interest);
}
