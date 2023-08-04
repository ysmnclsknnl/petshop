package com.example.petshop.dto;

import com.example.petshop.user.data.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String email;
    private Role role;
    private String accessToken;
    private String refreshToken;
}
