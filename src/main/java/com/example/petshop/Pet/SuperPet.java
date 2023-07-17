package com.example.petshop.Pet;

import com.example.petshop.Type;
import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperPet {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id;
    private String name;
    private String description;
    private Integer age;
    private Type type;
    private Boolean adopted = false;

    public SuperPet(String name, String description, Integer age, Type type, Boolean adopted) {
        this.name = name;
        this.description = description;
        this.age = age;
        this.type = type;
        this.adopted = adopted;
    }
}
