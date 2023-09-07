package com.example.petshop.pet;


import com.example.petshop.MongoDBTestContainerConfig;
import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.Type;
import com.example.petshop.security.SecurityOff;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
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
            false,
            "https://www.dog.com"
    );

    private final Pet validCat = new Pet(
            "Cat",
            "Cute cat. Likes to play with string",
            2,
            Type.CAT,
            false,
            "https://www.cat.com"
    );

    @Test
    public void whenGetAllPets_thenSuccess() throws Exception {
        repository.save(validDog);
        repository.save(validCat);

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk());
    }

}
