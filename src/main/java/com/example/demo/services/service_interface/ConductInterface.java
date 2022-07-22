package com.example.demo.services.service_interface;

import com.example.demo.entities.Conduct;

import java.util.List;

public interface ConductInterface {
    void saveConduct(Conduct conduct);
    List<Conduct> findAllConducts();

    void saveAllConduct(List<Conduct> conductList);

    Conduct findByConduct(String conduct);
}
