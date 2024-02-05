package com.example.petshop.pet.data;

import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor(onConstructor = @__(@PersistenceCreator))
@Document(collection = "pet")
public class Pet {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId id = new ObjectId();
    private final String name;
    private final String description;
    private final Integer age;
    private final Type type;
    private Boolean adopted = false;
    private final String photoLink;
}
