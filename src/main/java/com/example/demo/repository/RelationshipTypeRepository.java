package com.example.demo.repository;

import com.example.demo.entities.RelationType;
import org.springframework.data.repository.CrudRepository;

public interface RelationshipTypeRepository extends CrudRepository<RelationType,Long> {
}
