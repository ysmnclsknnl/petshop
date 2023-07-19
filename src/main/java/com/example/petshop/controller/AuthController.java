package com.example.petshop.controller;

import com.example.petshop.collection.Pet;
import com.example.petshop.collection.User;
import com.example.petshop.dto.LoginDTO;
import com.example.petshop.dto.SignupDTO;
import com.example.petshop.dto.TokenDTO;
import com.example.petshop.security.TokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    TokenGenerator tokenGenerator;
    @Autowired
    DaoAuthenticationProvider daoAuthenticationProvider;
    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    JwtAuthenticationProvider refreshTokenAuthProvider;
    @Autowired
    UserDetailsManager userDetailsManager;

    @PostMapping("/register")
    public ModelAndView register(@ModelAttribute SignupDTO signupDTO, HttpServletResponse response) {
        User user = new User(signupDTO.getUserName(), signupDTO.getEmail(), signupDTO.getPassword(), "CUSTOMER");
        userDetailsManager.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(),  Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDTO token = tokenGenerator.createToken(authentication);
        response.setHeader("Authorization", "Bearer " + token.getAccessToken());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/auth/setToken/" + token.getAccessToken());
//        modelAndView.setViewName("redirect:/pets");
        return modelAndView;
    }
    @GetMapping("/setToken/{token}")
    public ModelAndView setToken(@PathVariable("token") String token, HttpServletRequest request) {
        // Store the token into the session
        request.getSession().setAttribute("token", token);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/pets");
        return modelAndView;
    }


    @GetMapping("/login")
    public ModelAndView loginForm() {
       return new ModelAndView("login", Collections.singletonMap("user", new SignupDTO()));
    }

    @PostMapping("/login")
    public ResponseEntity login(@ModelAttribute SignupDTO user) {
        Authentication authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(user.getEmail(), user.getPassword()));

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }

    @PostMapping("/token")
    public ResponseEntity token(@RequestBody TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();

        return ResponseEntity.ok(tokenGenerator.createToken(authentication));
    }
}