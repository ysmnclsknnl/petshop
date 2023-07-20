package com.example.petshop.service;

import com.example.petshop.collection.Pet;
import com.example.petshop.dto.CreatePetDTO;
import com.example.petshop.dto.PetDTO;
import com.example.petshop.repository.PetRepository;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PetService {
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public List<PetDTO> allPets() {

        return petRepository.findAllByOrderByIdDesc().stream()
                .map(PetDTO::from)
                .toList();
    }

    /*public String createPet(PetDTO petDTO, MultipartFile photo) {

        try {
            List<String> errors = validatePet(petDTO, photo);
            if (errors.isEmpty()) {
                Binary petImage = new Binary(photo.getBytes());
                Pet pet = new Pet(
                        petDTO.getName(),
                        petDTO.getDescription(),
                        petDTO.getAge(),
                        petDTO.getType(),
                        false,
                        petImage
                );

                return petRepository.save(pet).getId().toHexString();
            }

            throw new IllegalArgumentException(String.join(" ", errors));
        }
        catch(IllegalArgumentException ex) {
            throw new IllegalArgumentException(ex.getMessage());
            }
        catch (IOException ex) {
            throw new RuntimeException("Something went wrong while loading image");
        }
        }*/

    public String createPet(CreatePetDTO petDTO) {
            List<String> errors = validatePetDTO(petDTO);
            if (errors.isEmpty()) {
               Pet pet = Pet.from(petDTO);
                return petRepository.save(pet).getId().toHexString();
            }

            throw new IllegalArgumentException(String.join(" ", errors));
    }


    public String adoptPet(ObjectId petId) {
        Optional<Pet> optionalPet = petRepository.findById(petId);
        Pet pet = optionalPet.orElseThrow(() -> new NoSuchElementException("Pet not found with ID: " + petId));

        if ((pet.getAdopted())) {
            throw new IllegalArgumentException("Pet with ID: " + petId + " is already adopted.");
        } else {
            pet.setAdopted(true);

            return petRepository.save(pet).getName();
        }

    }

    private List<String> validatePet(PetDTO pet, MultipartFile photo) {
        List<String> errors = new ArrayList<>();

        validateName(pet.getName()).ifPresent(errors::add);
        validateDescription(pet.getDescription()).ifPresent(errors::add);
        validateAge(pet.getAge()).ifPresent(errors::add);
        validatePhoto(photo).ifPresent(errors::add);

        return errors;
    }

    private List<String> validatePetDTO(CreatePetDTO pet) {
        List<String> errors = new ArrayList<>();

        validateName(pet.getName()).ifPresent(errors::add);
        validateDescription(pet.getDescription()).ifPresent(errors::add);
        validateAge(pet.getAge()).ifPresent(errors::add);
        validatePetDTOPhoto(pet.getPhoto()).ifPresent(errors::add);

        return errors;
    }

    private Optional<String> validateName(String name) {
        return (name != null && name.length() >= 3) ?
                Optional.empty() :
                Optional.of("Name must be at least 3 characters.");
    }

    private Optional<String> validateDescription(String description) {
        return (description != null && description.length() >= 15) ?
                Optional.empty() :
                Optional.of("Description must be at least 15 characters.");
    }

    private Optional<String> validateAge(Integer age) {
        return (age != null && age >= 0) ?
                Optional.empty() :
                Optional.of("Age must be at least 0.");
    }

    private Optional<String> validatePhoto(MultipartFile photo) {
        try {
            return (!photo.isEmpty() && photo.getBytes().length <= 100 * 1024) ?
                    Optional.empty() :
                    Optional.of("A pet should have an image of maximum 100kb");
        } catch(IOException ex) {
            return  Optional.of("Error occurred while processing the image");
        }
    }

    private Optional<String> validatePetDTOPhoto(String photo) {
            return (!photo.isEmpty() && photo.getBytes().length <= 100 * 1024) ?
                    Optional.empty() :
                    Optional.of("A pet should have an image of maximum 100kb");
    }
}
