package com.example.petshop.pet.service;


import com.example.petshop.config.security.KeyUtils;
import com.example.petshop.config.security.WebSecurity;
import com.example.petshop.pet.controller.CreatePetDTO;
import com.example.petshop.pet.controller.PetController;
import com.example.petshop.pet.controller.PetDTO;
import com.example.petshop.pet.data.Type;
import com.example.petshop.user.controller.AuthController;
import com.example.petshop.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PetController.class)
@ImportAutoConfiguration(WebSecurity.class)
class SecurityTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PetService petService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthController authController;

    @MockBean
    private KeyUtils keyUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtEncoder jwtEncoder;


    @WithAnonymousUser
    @Test
    void givenUnauthenticatedUser_whenGetPet_thenUnauthorized() throws Exception {
        final List<PetDTO> pets = List.of(
                new PetDTO(
                        "1",
                        "Cat",
                        "Very fast cat. Loves eating fish.",
                        1,
                        Type.CAT,
                        false,
                        "www.foo.com"
                ),
                new PetDTO(
                        "2",
                        "Cat",
                        "Very fast dog. Loves eating meat.",
                        1,
                        Type.DOG,
                        false,
                        "www.foo.com"
                )
        );

        given(petService.allPets()).willReturn(pets);

        mvc.perform(get("/api/pets"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void givenAdminUser_whenGetPet_thenSuccess() throws Exception {
        final List<PetDTO> pets = List.of(
                new PetDTO(
                        "1",
                        "Cat",
                        "Very fast cat. Loves eating fish.",
                        1,
                        Type.CAT,
                        false,
                        "www.foo.com"
                ),
                new PetDTO(
                        "2",
                        "Cat",
                        "Very fast dog. Loves eating meat.",
                        1,
                        Type.DOG,
                        false,
                        "www.foo.com"
                )
        );

        given(petService.allPets()).willReturn(pets);

        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "CUSTOMER")
    @Test
    void givenCustomerUser_whenGetPet_thenSuccess() throws Exception {
        final List<PetDTO> pets = List.of(
                new PetDTO(
                        "1",
                        "Cat",
                        "Very fast cat. Loves eating fish.",
                        1,
                        Type.CAT,
                        false,
                        "www.foo.com"
                ),
                new PetDTO(
                        "2",
                        "Cat",
                        "Very fast dog. Loves eating meat.",
                        1,
                        Type.DOG,
                        false,
                        "www.foo.com"
                )
        );

        given(petService.allPets()).willReturn(pets);

        mvc.perform(get("/api/pets"))
                .andExpect(status().isOk());
    }

    @WithMockUser(authorities = "ROLE_ADMIN")
    @Test
    void givenAdminUser_WhenCreatePet_thenCreated() throws Exception {

        final CreatePetDTO pet = new CreatePetDTO(
                "Cat",
                "Very fast cat. Loves eating fish.",
                1,
                Type.CAT,
                "www.foo.com"
        );

        final ObjectId petId = new ObjectId();

        given(petService.createPet(pet)).willReturn(petId);

        mvc.perform(
                        post("/api/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(pet))
                )
                .andExpect(status().isCreated());
    }

    /*// ... (other test methods)

    @WithMockUser(roles = "ADMIN")
    @Test
    public void givenUsersWithRolesExceptCustomer_whenAdoptPet_thenForbidden() throws Exception {
        // ... (create pet object)

        BDDMockito.given(petService.adoptPet(pet.getId())).willReturn("You successfully adopted " + pet.getName() + "!");

        mvc.perform(
                        patch("/api/pets/" + pet.getId()))
                .andExpect(status().isForbidden());
    }

    // ... (other test methods)

    @WithAnonymousUser
    @Test
    public void givenUnauthenticatedUser_whenAdoptPet_thenUnauthorized() throws Exception {
        // ... (create pet object)

        BDDMockito.given(petService.adoptPet(pet.getId())).willReturn("You successfully adopted " + pet.getName() + "!");

        mvc.perform(
                        patch("/api/pets/" + pet.getId()))
                .andExpect(status().isUnauthorized());
    }
}
{*/
}
