package com.example.demo.services.service_interface;

import com.example.demo.entities.RelationType;

import java.util.List;

public interface RelationshipTypeInterface {
    void saveRelation(RelationType relationType);

    List<RelationType> findAllTypes();

    void saveAllRelation(List<RelationType> relationTypes);

    RelationType findRelationById(Long relationId);
}
