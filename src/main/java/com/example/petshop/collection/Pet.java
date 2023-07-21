package com.example.petshop.collection;

import com.example.petshop.dto.CreatePetDTO;
import com.example.petshop.dto.Type;
import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pet")
public class Pet {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private Integer age;
    private Type type;
    private Boolean adopted;
    private Binary photo;

    public Pet(String name, String description, Integer age, Type type, Boolean adopted, Binary photo) {
        this.name = name;
        this.description = description;
        this.age = age;
        this.type = type;
        this.adopted = adopted;
        this.photo = photo;
    }

    public static Pet from(CreatePetDTO pet) {
        byte[] binaryData = Base64.getDecoder().decode(pet.getPhoto());
        return new Pet(pet.getName(), pet.getDescription(), pet.getAge(), pet.getType(), false, new Binary(binaryData));

    }
}
