package com.example.petshop;

import com.example.petshop.collection.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@EqualsAndHashCode(callSuper = true) // needed when using lombok data with supers, however you are not using the equals and hashcode, so consider removing the @data anotation
@AllArgsConstructor
@NoArgsConstructor
//move to appropriate package
public class PetWithStringImage extends SuperPet implements Comparable<PetWithStringImage> {
    private String photo;

    public PetWithStringImage(Pet pet) {
        super(pet.getId(), pet.getName(), pet.getDescription(), pet.getAge(), pet.getType(), pet.getAdopted());
        this.photo = Base64.getEncoder().encodeToString(pet.getPhoto().getData());
    }

    @Override
    public int compareTo(PetWithStringImage other) {
// using existing comparing code for simple comparisson like this is more foul proof, eg Long.compare()
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
