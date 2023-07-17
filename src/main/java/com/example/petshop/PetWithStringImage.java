package com.example.petshop;

import com.example.petshop.collection.Pet;
import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetWithStringImage extends SuperPet implements Comparable<PetWithStringImage> {
    private String photo;

    public PetWithStringImage(Pet pet) {
        super(pet.getId(), pet.getName(),pet.getDescription(),pet.getAge(),pet.getType(),pet.getAdopted());
        this.photo = Base64.getEncoder().encodeToString(pet.getPhoto().getData());
    }

    @Override
    public int compareTo(PetWithStringImage other) {
        long thisTimestamp = this.getId().getTimestamp();
        long otherTimestamp = other.getId().getTimestamp();

        if (thisTimestamp < otherTimestamp) {
            return 1;
        } else if (thisTimestamp > otherTimestamp) {
            return -1;
        }
        return 0;
    }

}
