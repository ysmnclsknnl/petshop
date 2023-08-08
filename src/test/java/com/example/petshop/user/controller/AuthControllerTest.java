package com.example.petshop.user.controller;

import com.example.petshop.MongoDBTestContainerConfig;
import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.PetRepository;
import com.example.petshop.pet.data.Type;
import com.example.petshop.user.data.User;
import com.example.petshop.user.data.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ImportAutoConfiguration(classes = MongoDBTestContainerConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void testRegisterUser() throws Exception {
        String requestBody = """
                {
                "userName": "aaa@gmail.com",
                "password": "123456",
                "role": "CUSTOMER"
                }\
                """;
        final String content = mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assertEquals(content, "aaa@gmail.com");

        User savedUser = userRepository.findByEmail("aaa@gmail.com").get();
        assertEquals(savedUser.getUsername(), "aaa@gmail.com");
    }

}
