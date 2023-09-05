package com.example.petshop.pet.service;

import com.example.petshop.pet.controller.CreatePetDTO;
import com.example.petshop.pet.controller.PetDTO;
import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.PetRepository;
import org.bson.types.ObjectId;
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

    public List<PetDTO> allPets() {

        return petRepository.findAllByOrderByIdDesc().stream()
                .map(PetDTO::from)
                .toList();
    }

    public ObjectId createPet(CreatePetDTO petDTO) {
        final String errors = validatePetDTO(petDTO);

        if (!errors.isBlank()) {
            throw new IllegalArgumentException(errors);
        }

           final Pet pet = Pet.from(petDTO);

            return petRepository.save(pet).getId();

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


    public String validatePetDTO(CreatePetDTO pet) {
        return List.of(
                        validateName(pet.getName()),
                        validateDescription(pet.getDescription()),
                        validateAge(pet.getAge()),
                        validatePetDTOPhoto(pet.getPhoto())
        )
                .stream().filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.joining(" "));
    }

    protected static Optional<String> validateName(String name) {
        return (name != null && name.length() >= 3) ?
                Optional.empty() :
                Optional.of("Name must be at least 3 characters.");
    }

    protected static Optional<String> validateDescription(String description) {
        return (description != null && description.length() >= 15) ?
                Optional.empty() :
                Optional.of("Description must be at least 15 characters.");
    }

    protected static Optional<String> validateAge(Integer age) {

        return (age != null && age >= 0) ?
                Optional.empty() :
                Optional.of("Age must be at least 0.");
    }

    protected static Optional<String> validatePetDTOPhoto(String photo) {
        return (!photo.isEmpty() && photo.getBytes().length <= 100 * 1024) ?
                Optional.empty() :
                Optional.of("A pet should have an image of maximum 100kb");
    }
}