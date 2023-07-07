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
public class PetWithStringImage {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private Integer age;
    private Type type;
    private Boolean adopted = false;
    private String photo;

    public PetWithStringImage(Pet pet) {
            this.id = pet.getId();
            this.name = pet.getName();
            this.description = pet.getDescription();
            this.age = pet.getAge();
            this.type = pet.getType();
            this.photo = Base64.getEncoder().encodeToString(pet.getPhoto().getData());
    }
}
