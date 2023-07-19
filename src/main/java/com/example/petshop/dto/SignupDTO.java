package com.example.petshop.dto;

import com.example.petshop.User.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDTO {
    private String userName;
    private String email;
    private String password;
    private Role role;
}
