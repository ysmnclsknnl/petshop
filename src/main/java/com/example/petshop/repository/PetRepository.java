package com.example.petshop.repository;

import com.example.petshop.collection.Pet;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PetRepository extends MongoRepository<Pet, ObjectId> {
    List<Pet> findByAdopted(boolean adopted, Sort sort);
}
