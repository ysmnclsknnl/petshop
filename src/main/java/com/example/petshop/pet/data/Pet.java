package com.example.petshop.pet.data;

import com.example.petshop.pet.controller.CreatePetDTO;
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
// keep associated classes close together in the package structure
// -> Controller + dto in same (super)package, Repo+entity/document in same (super)package
// -> config in config package, util in util or close to where it is used (if only used in one place)
// a common mvc structure is to have a config package, a controller package, a service package and a data package but your free to change those around
// just keep associated classes close together!
public class
Pet {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class) //is this serializer used?
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
