package com.example.petshop.pet.data;

import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Data
@Document(collection = "pet")
public class Pet {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private final ObjectId id;
    private final String name;
    private final String description;
    private final Integer age;
    private final Type type;
    private Boolean adopted;
    private final String photoLink;

    public Pet(ObjectId id , String name, String description, Integer age, Type type, Boolean adopted, String photoLink) {
        this.id =  id == null ? new ObjectId() : id;
        this.name = name;
        this.description = description;
        this.age = age;
        this.type = type;
        this.adopted = adopted == null ? false : adopted;
        this.photoLink = photoLink;
    }



    public Optional<IllegalArgumentException> getValidationExceptions() {
        List<String> errors = Stream.of(
                        name != null && name.length() >= 3 ? null : "Name must be at least 3 characters.",
                        description != null && description.length() >= 15 ? null : "Description must be at least 15 characters.",
                        age != null && age >= 0 ? null : "Age must be at least 0.",
                        photoLink != null  ? null : "Photo must have an image.",
                        photoLink!= null &&  photoLink.matches("^(http|https)://[^ \"]+$")? null : "Image link should should start with http or https and not contain spaces."
                ).filter(Objects::nonNull)
                .toList();

        return errors.isEmpty() ?
                Optional.empty() :
                Optional.of(new IllegalArgumentException(String.join(" ", errors)));
    }
}
