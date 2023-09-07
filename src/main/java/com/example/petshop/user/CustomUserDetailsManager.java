package com.example.petshop.user;

import com.example.petshop.user.data.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class CustomUserDetailsManager implements UserDetailsManager {
    private final
    UserRepository userRepository;

    private final
    PasswordEncoder passwordEncoder;


    public CustomUserDetailsManager(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserDetails user) {
        if (!userExists(user.getUsername())) {
            ((User) user).setPassword(getPasswordEncoder().encode(user.getPassword()));
           getUserRepository().save((User) user);
        } else {
            throw new IllegalArgumentException("User exists with email" + user.getUsername());
        }

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {
        getUserRepository().deleteByEmail(username);
    }

    @Override
    public boolean userExists(String email) {
        return getUserRepository().existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getUserRepository().findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("user {0} not found", email)
                ));
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}