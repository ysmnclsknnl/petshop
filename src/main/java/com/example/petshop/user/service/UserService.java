package com.example.petshop.user.service;

import com.example.petshop.config.security.token.TokenGenerator;
import com.example.petshop.user.controller.dto.LoginDTO;
import com.example.petshop.user.controller.dto.SignupDTO;
import com.example.petshop.user.controller.dto.TokenDTO;
import com.example.petshop.user.data.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {
    final
    CustomUserDetailsManager customUserDetailsManager;

    final
    TokenGenerator tokenGenerator;

    final
    JwtAuthenticationProvider refreshTokenAuthProvider;

    public UserService(CustomUserDetailsManager customUserDetailsManager, TokenGenerator tokenGenerator, JwtAuthenticationProvider refreshTokenAuthProvider) {
        this.customUserDetailsManager = customUserDetailsManager;
        this.tokenGenerator = tokenGenerator;
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
    }

    public TokenDTO registerUser(SignupDTO signupDTO) {
        String errors = validateUser(signupDTO);
        if (!errors.isBlank()) {
            throw new IllegalArgumentException(errors);
        }

        final User user = new User(
                signupDTO.getUserName(),
                signupDTO.getEmail(),
                signupDTO.getPassword(),
                signupDTO.getRole()
        );
        customUserDetailsManager.createUser(user);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                user,
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );


        return tokenGenerator.createToken(authentication);
    }

    public TokenDTO loginUser(LoginDTO loginDTO) {
        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(),
                loginDTO.getPassword()
        );

        return tokenGenerator.createToken(authentication);
    }

    public TokenDTO getToken(TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider
                .authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));

        return tokenGenerator.createToken(authentication);
    }

    public static String validateUser(SignupDTO user) {
        StringBuilder errors = new StringBuilder();

        if (user.getUserName().length() < 3) {
            errors.append("Name must be at least 3 characters. ");
        }

        final String email = user.getEmail();
        final String emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        if (email == null || !email.matches(emailRegex)) {
            errors.append("Email address can consist of numbers, letters and '.', '-', '_' symbols");
        }

        final String password = user.getPassword();


        final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@$!%*?&]).{8,}$";

        if (password == null || !password.matches(passwordRegex)){
            errors.append("Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special symbol (@$!%*?&).");
        }

        return errors.toString();
    }
}
