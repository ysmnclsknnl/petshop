package com.example.petshop.controller;

import com.example.petshop.collection.User;
import com.example.petshop.dto.LoginDTO;
import com.example.petshop.dto.SignupDTO;
import com.example.petshop.dto.TokenDTO;
import com.example.petshop.security.TokenGenerator;
import com.example.petshop.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    final
    TokenGenerator tokenGenerator;
    final
    DaoAuthenticationProvider daoAuthenticationProvider;
    final
    JwtAuthenticationProvider refreshTokenAuthProvider;

    final
    UserService userDetailsManager;

    public AuthController(TokenGenerator tokenGenerator, DaoAuthenticationProvider daoAuthenticationProvider, @Qualifier("jwtRefreshTokenAuthProvider") JwtAuthenticationProvider refreshTokenAuthProvider, UserService userDetailsManager) {
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
        this.userDetailsManager = userDetailsManager;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody SignupDTO signupDTO) {
        try {

            String errors = UserService.validateUser(signupDTO);
            if (!errors.isBlank()) {
                throw new IllegalArgumentException(errors);
            }

            User user = new User(signupDTO.getUserName(), signupDTO.getEmail(), signupDTO.getPassword(), signupDTO.getRole());
            userDetailsManager.createUser(user);

            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                    user,
                    signupDTO.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );

            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        } catch (Exception ex) {
            if (ex instanceof IllegalArgumentException) {

                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());

            }
        }
    }
  /*  @GetMapping("/register")
  public ModelAndView addLoginForm() {
     return new ModelAndView("login", Collections.singletonMap("signUpDTO", new SignupDTO()));
   }*/

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = daoAuthenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),
                            loginDTO.getPassword())
            );

            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password.", e);
        }
    }

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }
}