package com.example.petshop.pet;


import com.example.petshop.MongoDBTestContainerConfig;
import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.Type;
import com.example.petshop.security.SecurityOff;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ImportAutoConfiguration(SecurityOff.class)
public class PetControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    private final Pet validDog = new Pet(
            "Dog",
            "Cute dog. Likes to play fetch",
            1,
            Type.DOG,
            "https://www.dog.com"
    );

    private final Pet validCat = new Pet(
            "Cat",
            "Cute cat. Likes to play with string",
            2,
            Type.CAT,
            "https://www.cat.com"
    );

    @Test
    public void whenGetAllPets_thenSuccess() throws Exception {
        repository.save(validDog);
        repository.save(validCat);

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreatePet_WithValidData_thenSuccess() throws Exception {
        mockMvc.perform(post("/pets/add")
                .contentType("application/json")
                .content("{\n" +
                        "    \"name\": \"Dog\",\n" +
                        "    \"description\": \"Cute dog. Likes to play fetch\",\n" +
                        "    \"age\": 1,\n" +
                        "    \"type\": \"DOG\",\n" +
                        "    \"photoLink\": \"https://www.dog.com\"\n" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenAdoptPet_WithValidId_thenSuccess() throws Exception {
        ObjectId id = repository.save(validDog).getId();

        mockMvc.perform(patch("/pets/" + id))
                .andExpect(status().isOk());
    }
}
