package com.example.petshop.controller;

import com.example.petshop.PetWithStringImage;
import com.example.petshop.collection.Pet;
import com.example.petshop.service.PetService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<PetWithStringImage>> getAllPets() {
        try {
            return new ResponseEntity<>(petService.allPets(), HttpStatus.OK);
        } catch (Exception error) {

            String errorMessage = "An error occurred while retrieving pets.";
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    @PostMapping("/create")
      public ResponseEntity<String> createPet(@RequestBody PetWithStringImage pet) {

        try {

            String createdPetId = petService.createPet(pet).toHexString();

            return new ResponseEntity<>(createdPetId, HttpStatus.CREATED);
        } catch (Exception ex) {

            if(ex instanceof IllegalArgumentException) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Pet> adoptPet(@PathVariable ObjectId id) {
        try {
            return new ResponseEntity<>(petService.adoptPet(id), HttpStatus.OK);
        } catch (Exception ex) {
            if (ex instanceof IllegalArgumentException || ex  instanceof NoSuchElementException) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");

            }
        }
    }
}

