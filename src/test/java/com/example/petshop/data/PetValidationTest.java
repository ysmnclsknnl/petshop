package com.example.petshop.data;

import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.Type;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PetValidationTest {

    @Test
    public void whenPetDataIsValid_returnNoExceptions() {
        Pet validDog = new Pet(
                new ObjectId(),
                "Dog",
                "Cute dog. Likes to play fetch",
                1,
                Type.DOG,
                false,
                "https://www.link.com");

        assertTrue(validDog.getValidationExceptions().isEmpty());
    }

    @Test
    public void whenNameIsLessThan3Characters_returnException() {
        Pet invalidDog = new Pet(
                new ObjectId(),
                "Do",
                "Cute dog. Likes to play fetch",
                1,
                Type.DOG,
                false,
                "https://www.link.com");

        Optional<IllegalArgumentException> validationExceptions = invalidDog.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Name must be at least 3 characters.", validationExceptions.get().getMessage());
    }

    @Test
    public void whenDescriptionIsLessThan15Characters_returnException() {
        Pet invalidDog = new Pet(
                new ObjectId(),
                "Dog",
                "Cute dog",
                1,
                Type.DOG,
                false,
                "https://www.link.com");

        Optional<IllegalArgumentException> validationExceptions = invalidDog.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Description must be at least 15 characters.", validationExceptions.get().getMessage());
    }


    @Test
    public void whenAgeIsLessThan0_returnException() {
        Pet invalidDog = new Pet(
                new ObjectId(),
                "Dog",
                "Cute dog. Likes to play fetch",
                -1,
                Type.DOG,
                false,
                "https://www.link.com");

        Optional<IllegalArgumentException> validationExceptions = invalidDog.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Age must be at least 0.", validationExceptions.get().getMessage());
    }

    @Test
    public void whenPhotoLinkIsNull_returnException() {
        Pet invalidDog = new Pet(
                new ObjectId(),
                "Dog",
                "Cute dog. Likes to play fetch",
                1,
                Type.DOG,
                false,
                null);

        Optional<IllegalArgumentException> validationExceptions = invalidDog.getValidationExceptions();
        assertTrue(validationExceptions.isPresent());
        assertEquals("Pet must have an image link. Image link should should start with http or https and not contain spaces.", validationExceptions.get().getMessage());
    }

}
