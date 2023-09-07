package com.example.petshop.pet.data;

import com.example.petshop.pet.data.CreatePetDTO;
import com.example.petshop.pet.data.Type;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PetValidationTest {
    CreatePetDTO validDog = new CreatePetDTO(
            "Dog",
            "Cute dog. Likes to play fetch",
            1,
            Type.DOG,
            "https://www.link.com");
    @Test
    public void whenPetDataIsValid_returnNoExceptions() {

        assertTrue(validDog.getValidationExceptions().isEmpty());
    }

    @Test
    public void whenNameIsLessThan3Characters_returnException() {
        CreatePetDTO dogWithInvalidName = validDog.withName("Do");

        Optional<IllegalArgumentException> validationExceptions = dogWithInvalidName.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Name must be at least 3 characters.", validationExceptions.get().getMessage());
    }

    @Test
    public void whenDescriptionIsLessThan15Characters_returnException() {
        CreatePetDTO dogWithInvalidDescription = validDog.withDescription("Cute dog");

        Optional<IllegalArgumentException> validationExceptions = dogWithInvalidDescription.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Description must be at least 15 characters.", validationExceptions.get().getMessage());
    }


    @Test
    public void whenAgeIsLessThan0_returnException() {
        CreatePetDTO dogWithInvalidAge = validDog.withAge(-1);

        Optional<IllegalArgumentException> validationExceptions = dogWithInvalidAge.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Age must be at least 0.", validationExceptions.get().getMessage());
    }

    @Test
    public void whenPhotoLinkIsNull_returnException() {
        CreatePetDTO dogWithInvalidImage = validDog.withPhotoLink(null);

        Optional<IllegalArgumentException> validationExceptions = dogWithInvalidImage.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Pet must have an image link. Image link should should start with http or https and not contain spaces.", validationExceptions.get().getMessage());
    }

}
