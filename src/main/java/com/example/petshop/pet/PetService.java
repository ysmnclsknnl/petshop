package com.example.petshop.pet;

import com.example.petshop.pet.data.CreatePetDTO;
import com.example.petshop.pet.data.Pet;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;



@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<Pet> allPets() {

        return petRepository.findAllByOrderByIdDesc();
    }

    public ObjectId createPet(CreatePetDTO pet) {
       pet.getValidationExceptions().ifPresent(ex -> {
            throw ex;
        });

        return petRepository.save(toPet(pet)).getId();

    }

    public Pet toPet(CreatePetDTO createPetDTO) {
        return new Pet(
                createPetDTO.name(),
                createPetDTO.description(),
                createPetDTO.age(),
                createPetDTO.type(),
                createPetDTO.photoLink()
        );
    }

    public String adoptPet(ObjectId petId) {
        Pet pet = petRepository
                .findById(petId)
                .orElseThrow(() -> new NoSuchElementException("Pet not found with ID: " + petId));

        if ((pet.getAdopted())) {
            throw new IllegalArgumentException("Pet with ID: " + petId + " is already adopted.");
        }
            pet.setAdopted(true);

            return "You adopted " + petRepository.save(pet).getName() + "!";
        }


}
