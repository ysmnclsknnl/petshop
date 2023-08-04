package com.example.petshop.pet.controller;

import com.example.petshop.pet.data.Pet;
import com.example.petshop.pet.data.Type;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
    @Id
    private String id;
    private String name;
    private String description;
    private Integer age;
    private Type type;
    private Boolean adopted = false;
    private String photo;

    public static PetDTO from(Pet pet) {
        return new PetDTO(pet.getId().toHexString(),
                pet.getName(),
                pet.getDescription(),
                pet.getAge(),
                pet.getType(),
                pet.getAdopted(),
                Base64.getEncoder().encodeToString(pet.getPhoto().getData())
        );
    }
}
