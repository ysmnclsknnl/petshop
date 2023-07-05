package com.example.petshop.controller;

import com.example.petshop.collection.Pet;
import com.example.petshop.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<?> getAllPets() {
        try {
            return new ResponseEntity<>(petService.allPets(), HttpStatus.OK);
        } catch (Exception error) {

            String errorMessage = "An error occurred while retrieving pets.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
      public ResponseEntity<?> createPet(@RequestBody Pet pet) {

        try {

            String createdPetId = petService.createPet(pet).toHexString();

            return new ResponseEntity<>(createdPetId, HttpStatus.CREATED);
        } catch (Exception ex) {

            if(ex instanceof IllegalArgumentException) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> adoptPet(@PathVariable ObjectId id) {
        try {
            return new ResponseEntity<>(petService.adoptPet(id), HttpStatus.OK);
        } catch (Exception ex) {
            if (ex instanceof IllegalArgumentException || ex  instanceof NoSuchElementException) {

                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}

