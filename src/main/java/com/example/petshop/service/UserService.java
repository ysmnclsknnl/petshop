package com.example.petshop.service;

import com.example.petshop.collection.User;
import com.example.petshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String email, String password) {
       User user =  userRepository.findByEmail(email);
       if(user != null) {
           if(password == user.getPassword()) {
               return user;
           }
           throw new IllegalArgumentException("Password is not correct!");
       }

       throw  new IllegalArgumentException("No user is found with this email");
    }

    public User createUser(User user) {
        String invalidInputs = getInvalidInputs(user);
        if (invalidInputs.isBlank()) {
            return userRepository.save(user);
        }
        throw new IllegalArgumentException(invalidInputs);
    }




    private String getInvalidInputs(User user) {
        StringBuilder errors = new StringBuilder();
        if (user.getName().length() < 3) {
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

       return errors.toString();

    }

}
