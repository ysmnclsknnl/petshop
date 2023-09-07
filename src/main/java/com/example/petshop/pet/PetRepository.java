package com.example.petshop.pet;

import com.example.petshop.pet.data.Pet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PetRepository extends MongoRepository<Pet, ObjectId> {
    List<Pet> findAllByOrderByIdDesc();
}
