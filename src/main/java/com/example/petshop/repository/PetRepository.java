package com.example.petshop.repository;

import com.example.petshop.collection.Pet;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PetRepository extends MongoRepository<Pet, ObjectId> {

}
