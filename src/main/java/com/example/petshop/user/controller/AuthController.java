package com.example.petshop.user.controller;

import com.example.petshop.user.controller.dto.LoginDTO;
import com.example.petshop.user.controller.dto.SignupDTO;
import com.example.petshop.user.controller.dto.TokenDTO;
import com.example.petshop.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    final
    UserService userService;

    public AuthController(UserService userDetailsManager) {
        this.userService = userDetailsManager;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody SignupDTO signupDTO) {
        try {
            return ResponseEntity.ok( userService.registerUser(signupDTO));
        } catch (IllegalArgumentException ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.ok(userService.loginUser(loginDTO));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password.", e);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenDTO tokenDTO) {
        try{
            return ResponseEntity.ok(userService.getToken(tokenDTO));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect refresh token.", e);
        }
    }
}