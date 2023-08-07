package com.example.petshop.pet.data;

import com.example.petshop.pet.controller.CreatePetDTO;
import com.example.petshop.pet.controller.PetDTO;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface PetRepository extends MongoRepository<Pet, ObjectId> {
    List<Pet> findAllByOrderByIdDesc();
}
