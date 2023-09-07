package com.example.petshop.controller;

import com.example.petshop.config.security.WebSecurity;
import com.example.petshop.pet.PetController;
import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.Type;
import com.example.petshop.pet.PetService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
@ImportAutoConfiguration(WebSecurity.class)
public class WebSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private PetService petService;

    private Pet validDog = new Pet(
            new ObjectId(),
            "Dog",
            "Cute dog. Likes to play fetch",
            1,
            Type.DOG,
            false,
            "https://www.link.com"
    );

    @WithMockUser(roles = {"ADMIN", "CUSTOMER"})
    @Test

    public void whenGetAllPets_thenSuccess() throws Exception {
        when(petService.allPets()).thenReturn(List.of(validDog));


        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk());

    }

    @WithAnonymousUser
    @Test
    public void whenGetAllPets_thenUnauthorized() throws Exception {
        when(petService.allPets()).thenReturn(List.of(validDog));
        mockMvc.perform(get("/pets"))
                .andExpect(status().isUnauthorized());


    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void whenAdmin_CreatePet_WithValidPetData_thenSuccessWithPetId() throws Exception {
        when(petService.createPet(validDog)).thenReturn(validDog.getId());
        mockMvc.perform(post("/pets/add")
                .contentType("application/json")
                .content(toJson(validDog)))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "CUSTOMER")
    @Test
    public void whenCustomer_CreatePet_thenForbidden() throws Exception {

        when(petService.createPet(validDog)).thenReturn(validDog.getId());
        mockMvc.perform(post("/pets/add")
                .contentType("application/json")
                .content(toJson(validDog)))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    public void whenAnonymousUser_CreatePet_thenUnauthorized() throws Exception {

        when(petService.createPet(validDog)).thenReturn(validDog.getId());
        mockMvc.perform(post("/pets/add")
                .contentType("application/json")
                .content(toJson(validDog)))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    public void whenAdmin_AdoptPet_thenForbidden() throws Exception {

        when(petService.adoptPet(validDog.getId())).thenReturn("You adopted " + validDog.getName() + "!");
        mockMvc.perform(patch("/pets/" + validDog.getId()))
                .andExpect(status().isForbidden());
    }

    @WithAnonymousUser
    @Test
    public void whenAnonymousUser_AdoptPet_thenUnauthorized() throws Exception {

        when(petService.adoptPet(validDog.getId())).thenReturn("You adopted " + validDog.getName() + "!");
        mockMvc.perform(patch("/pets/" + validDog.getId()))
                .andExpect(status().isUnauthorized());
    }

    private String toJson(Pet pet) {
        return String.format("{\n" +
                        "    \"name\": \"%s\",\n" +
                        "    \"description\": \"%s\",\n" +
                        "    \"age\": %d,\n" +
                        "    \"type\": \"%s\",\n" +
                        "    \"adopted\": %s,\n" +
                        "    \"photoLink\": \"%s\"\n" +
                        "}",
                pet.getName(),
                pet.getDescription(),
                pet.getAge(),
                pet.getType(),
                pet.getAdopted(),
                pet.getPhotoLink()
        );
    }
}