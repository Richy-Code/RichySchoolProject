package com.example.demo.services;

import com.example.demo.entities.RelationType;
import com.example.demo.repository.RelationshipTypeRepository;
import com.example.demo.services.service_interface.RelationshipTypeInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipTypeImp implements RelationshipTypeInterface {

    private final RelationshipTypeRepository repository;

    public RelationshipTypeImp(RelationshipTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void saveRelation(RelationType relationType) {
        repository.save(relationType);
    }


    @Override
    public List<RelationType> findAllTypes() {
        return (List<RelationType>) repository.findAll();
    }


    @Override
    public void saveAllRelation(List<RelationType> relationTypes) {
        repository.saveAll(relationTypes);
    }

    @Override
    public RelationType findRelationById(Long relationId) {
        return repository.findById(relationId).orElse(new RelationType());
    }
}
