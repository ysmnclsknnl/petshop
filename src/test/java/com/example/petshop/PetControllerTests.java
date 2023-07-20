package com.example.petshop;


import com.example.petshop.controller.PetController;
import com.example.petshop.security.KeyUtils;
import com.example.petshop.security.WebSecurity;
import com.example.petshop.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@WebMvcTest(PetController.class)
@Import(WebSecurity.class)
public class PetControllerTests {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PetService petService;

    @MockBean
    KeyUtils keyUtils;

    @MockBean
    UserDetailsManager userDetailsManager;

    @MockBean
    JwtDecoder jwtDecoder;


    @Test
    void petsCanBeListForAuthorizedUsers() throws Exception {
        this.mockMvc.perform(get("/pets").with(jwt().jwt(jwt -> jwt
                        .subject("hello@abc.com")
                        .claim("role","CUSTOMER"))))
                .andExpect(content().contentType("html"));
    }
}
