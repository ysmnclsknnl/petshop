package com.example.petshop.dto;

import com.example.petshop.collection.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetDTO {
    @Id
    private String id;
    private String name;
    private String description;
    private Integer age;
    private Type type;
    private Boolean adopted = false;
    private String photo;

    public PetDTO(String name, String description, Integer age, Type type) {
        this.name = name;
        this.description = description;
        this.age = age;
        this.type = type;
    }

    public PetDTO(Pet pet) {
        this.id = pet.getId().toHexString();
        this.name = pet.getName();
        this.description = pet.getDescription();
        this.age = pet.getAge();
        this.type = pet.getType();
        this.adopted = pet.getAdopted();
        this.photo = Base64.getEncoder().encodeToString(pet.getPhoto().getData());
    }
}
