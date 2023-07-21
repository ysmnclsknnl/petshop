package com.example.petshop.service;

import com.example.petshop.User.Role;
import com.example.petshop.collection.User;
import com.example.petshop.dto.SignupDTO;
import com.example.petshop.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class UserService implements UserDetailsManager {
    final
    UserRepository userRepository;

    final
    PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

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
    public static String validateUser(SignupDTO user) {
        StringBuilder errors = new StringBuilder();
        if (user.getUserName().length() < 3) {
            errors.append("Name must be at least 3 characters. ");
        }
        String email = user.getEmail();
        if (email == null || !email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            errors.append("Email address should consist of numbers, letters and '.', '-', '_' symbols");
        }
        String password = user.getPassword();
        if (password == null || password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")){
            errors.append("Password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special symbol (@$!%*?&).");
        }
        Role role = user.getRole();
        if (role == null || (role.name() != "CUSTOMER" && role.name() != "ADMIN")) {
            errors.append("No valid role is found. Role should be ADMIN or CUSTOMER");
        }
        return errors.toString();

    }
    }
