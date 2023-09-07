package com.example.petshop.pet.data;

import lombok.With;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@With
public record CreatePetDTO(
        String name,
        String description,
        Integer age,
        Type type,
        String photoLink) {
    public Optional<IllegalArgumentException> getValidationExceptions() {
        List<String> errors = Stream.of(
                        name != null && name.length() >= 3 ? null : "Name must be at least 3 characters.",
                        description != null && description.length() >= 15 ? null : "Description must be at least 15 characters.",
                        age != null && age >= 0 ? null : "Age must be at least 0.",
                        photoLink != null  ? null : "Pet must have an image link.",
                        photoLink!= null &&  photoLink.matches("^(http|https)://[^ \"]+$")? null : "Image link should should start with http or https and not contain spaces."
                ).filter(Objects::nonNull)
                .toList();

        return errors.isEmpty() ?
                Optional.empty() :
                Optional.of(new IllegalArgumentException(String.join(" ", errors)));
    }
}
