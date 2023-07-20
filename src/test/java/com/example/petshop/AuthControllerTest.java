package com.example.petshop;

import com.example.petshop.User.Role;
import com.example.petshop.collection.User;
import com.example.petshop.controller.AuthController;
import com.example.petshop.dto.SignupDTO;
import com.example.petshop.security.TokenGenerator;
import com.example.petshop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserDetailsManager userDetailsManager;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @Captor
    private ArgumentCaptor<UserDetails> userDetailsCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        SignupDTO signupDTO = new SignupDTO("username", "email@example.com", "password", Role.CUSTOMER);
        User user = new User(signupDTO.getUserName(), signupDTO.getEmail(), signupDTO.getPassword(), signupDTO.getRole());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user,
                signupDTO.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
        String expectedToken = "generated_token";

        when(userService.validateUser(signupDTO)).thenReturn("");
        when(userDetailsManager.createUser(any(UserDetails.class))).thenReturn(null);
        when(tokenGenerator.createToken(authentication)).thenReturn(expectedToken);

        ResponseEntity response = authController.register(signupDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToken, response.getBody());

        verify(userService).validateUser(signupDTO);
        verify(userDetailsManager).createUser(userDetailsCaptor.capture());
        verify(tokenGenerator).createToken(authentication);

        User capturedUser = (User) userDetailsCaptor.getValue();
        assertEquals(signupDTO.getUserName(), capturedUser.getUsername());
        assertEquals(signupDTO.getEmail(), capturedUser.getEmail());
        assertEquals(signupDTO.getPassword(), capturedUser.getPassword());
        assertEquals(signupDTO.getRole(), capturedUser.getRole());
    }

    @Test
    void testRegister_InvalidUser() {
        SignupDTO signupDTO = new SignupDTO("username", "email@example.com", "password", "ROLE_USER");
        String expectedErrorMessage = "Invalid user";

        when(userService.validateUser(signupDTO)).thenReturn(expectedErrorMessage);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authController.register(signupDTO));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals(expectedErrorMessage, exception.getReason());

        verify(userService).validateUser(signupDTO);
        verifyNoInteractions(userDetailsManager, tokenGenerator);
    }

    @Test
    void testRegister_InternalServerError() {
        SignupDTO signupDTO = new SignupDTO("username", "email@example.com", "password", "ROLE_USER");
        String expectedErrorMessage = "Internal server error";

        when(userService.validateUser(signupDTO)).thenReturn("");
        when(userDetailsManager.createUser(any(UserDetails.class))).thenThrow(new RuntimeException(expectedErrorMessage));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> authController.register(signupDTO));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        assertEquals(expectedErrorMessage, exception.getReason());

        verify(userService).validateUser(signupDTO);
        verify(userDetailsManager).createUser(any(UserDetails.class));
        verifyNoMoreInteractions(tokenGenerator);
    }
}
