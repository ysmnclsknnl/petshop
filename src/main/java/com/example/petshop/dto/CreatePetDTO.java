package com.example.petshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePetDTO {
    private String name;
    private String description;
    private Integer age;
    private Type type;
    private String photo;
}
