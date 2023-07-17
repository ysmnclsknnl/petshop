package com.example.petshop.Pet;

import com.example.petshop.collection.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetWithStringImage extends SuperPet  {
    private String photo;

    public PetWithStringImage(Pet pet) {
        super(pet.getId(), pet.getName(),pet.getDescription(),pet.getAge(),pet.getType(),pet.getAdopted());
        this.photo = Base64.getEncoder().encodeToString(pet.getPhoto().getData());
    }
}
