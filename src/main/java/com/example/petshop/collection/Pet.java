package com.example.petshop.collection;

import com.example.petshop.PetWithStringImage;
import com.example.petshop.Type;
import com.example.petshop.serializer.BinaryDeserializer;
import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
    private Boolean adopted = false;
    @JsonDeserialize(using = BinaryDeserializer.class)
    private Binary photo;

    public Pet(String name, String description, Integer age, Type type, Binary photo) {
        this.name = name;
        this.description = description;
        this.age = age;
        this.type = type;
        this.photo = photo;
    }
    public Pet(PetWithStringImage pet) {
        byte[] binaryData = Base64.getDecoder().decode(pet.getPhoto());
        this.name = pet.getName();
        this.description = pet.getDescription();
        this.age = pet.getAge();
        this.type = pet.getType();
        this.photo = new Binary(binaryData);
    }
}
