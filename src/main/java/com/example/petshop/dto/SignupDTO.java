package com.example.petshop.dto;

import com.example.petshop.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignupDTO {
    private String userName;
    private String email;
    private String password;
    private Role role;
}
