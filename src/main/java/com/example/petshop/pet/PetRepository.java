package com.example.petshop.pet;

import com.example.petshop.pet.data.Pet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends MongoRepository<Pet, ObjectId> {
    List<Pet> findAllByOrderByIdDesc();
}
