package com.example.petshop.service;

import com.example.petshop.PetWithStringImage;
import com.example.petshop.collection.Pet;
import com.example.petshop.repository.PetRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetWithStringImage> allPets() {
        List<Pet> notAdoptedPets = petRepository.findByAdopted(false, Sort.by(Sort.Direction.ASC, "id.timestamp"));

        return notAdoptedPets.stream()
                .map(PetWithStringImage::new)
                .collect(Collectors.toList());
    }

    public ObjectId createPet(Pet pet) {
        String invalidInputs = getInvalidInputs(pet);
        if (invalidInputs.isBlank()) {
            return petRepository.save(pet).getId();
        }

        throw new IllegalArgumentException(invalidInputs);
    }

    public Pet adoptPet(ObjectId petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElseThrow(() -> new NoSuchElementException("Pet not found with ID: " + petId));

        if (pet.getAdopted()) {
            throw new IllegalArgumentException("Pet with ID: " + petId + " is already adopted.");
        } else {
            pet.setAdopted(true);
            return petRepository.save(pet);
        }

    }

    private String getInvalidInputs(Pet pet) {

        StringBuilder errors = new StringBuilder();

        if (pet.getName().length() < 3) {
            errors.append("Name must be at least 3 characters. ");
        }
        if (pet.getDescription().length() < 15) {
            errors.append("Description must be at least 15 characters. ");
        }

        if (pet.getAge() < 0) {
            errors.append("Age must be at least 0. ");
        }

        if (pet.getPhoto() != null && pet.getPhoto().length() > 100 * 1024) {
            errors.append("A pet should have an image of maximum 100kb");
        }
        return errors.toString();
    }
}