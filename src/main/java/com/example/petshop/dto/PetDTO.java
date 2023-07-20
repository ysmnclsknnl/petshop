package com.example.petshop.dto;

import com.example.petshop.collection.Pet;
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
        return new PetDTO(pet.getId().toHexString(),pet.getName(),pet.getDescription(), pet.getAge(), pet.getType(), pet.getAdopted(), Base64.getEncoder().encodeToString(pet.getPhoto().getData()));

//        this.id = pet.getId().toHexString();
//        this.name = pet.getName();
//        this.description = pet.getDescription();
//        this.age = pet.getAge();
//        this.type = pet.getType();
//        this.adopted = pet.getAdopted();
//        this.photo = Base64.getEncoder().encodeToString(pet.getPhoto().getData());
    }
}
