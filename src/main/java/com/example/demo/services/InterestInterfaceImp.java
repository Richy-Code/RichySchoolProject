package com.example.demo.services;

import com.example.demo.entities.Interest;
import com.example.demo.repository.InterestRepository;
import com.example.demo.services.service_interface.InterestInterface;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InterestInterfaceImp implements InterestInterface {
    final InterestRepository repository;

    public InterestInterfaceImp(InterestRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveInterest(Interest interest) {
        repository.save(interest);
    }

    @Override
    public List<Interest> findAllInterest() {
        return (List<Interest>) repository.findAll();
    }

    @Override
    public void saveAllInterest(List<Interest> interestList) {
        repository.saveAll(interestList);
    }

    @Override
    public Interest findByInterest(String interest) {
        return repository.findByConduct(interest);
    }
}
