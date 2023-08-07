package com.example.petshop.user.service;

import com.example.petshop.config.security.TokenGenerator;
import com.example.petshop.user.controller.dto.LoginDTO;
import com.example.petshop.user.controller.dto.SignupDTO;
import com.example.petshop.user.controller.dto.TokenDTO;
import com.example.petshop.user.data.User;
import com.example.petshop.user.data.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;

@Service
public class UserService implements UserDetailsManager {
    final
    UserRepository userRepository;

    final
    PasswordEncoder passwordEncoder;

    final
    TokenGenerator tokenGenerator;

    final
    DaoAuthenticationProvider daoAuthenticationProvider;

    final
    JwtAuthenticationProvider refreshTokenAuthProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenGenerator tokenGenerator, DaoAuthenticationProvider daoAuthenticationProvider, JwtAuthenticationProvider refreshTokenAuthProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
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
        createUser(user);

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
               user.getEmail(),
               user.getPassword(),
               Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));

       return tokenGenerator.createToken(authentication);
    }

    public TokenDTO loginUser(LoginDTO loginDTO) {
        final Authentication authentication = daoAuthenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        return tokenGenerator.createToken(authentication);
    }

    public TokenDTO getToken(TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider
                .authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));

       return tokenGenerator.createToken(authentication);
    }

    @Override
    public void createUser(UserDetails user) {
        if (!userExists(user.getUsername())) {
            ((User) user).setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save((User) user);
        } else {
            throw new IllegalArgumentException("User exists with email" + user.getUsername());
        }

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

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {
        userRepository.deleteByEmail(username);
    }

    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("user {0} not found", email)
                ));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }
    }