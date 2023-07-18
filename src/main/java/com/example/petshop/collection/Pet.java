package com.example.petshop.collection;

import com.example.petshop.SuperPet;
import com.example.petshop.serializer.BinaryDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "pet")
public class Pet extends SuperPet{
    @JsonDeserialize(using = BinaryDeserializer.class) //is this used?
    private Binary photo;

    // I think its better to have to seperated objects for DTO and database and just map between them.
    public Pet(SuperPet superPet, MultipartFile photo) throws IOException {
        super(superPet.getName(), superPet.getDescription(), superPet.getAge(),superPet.getType(), false);
        if (!photo.isEmpty()) {
            byte[] photoBytes = photo.getBytes();
            String base64String = Base64.getEncoder().encodeToString(photoBytes); // encode and decode?
            byte[] binaryData = Base64.getDecoder().decode(base64String);
            this.setPhoto(new Binary(binaryData));
        } else {
            throw new IllegalArgumentException(" A pet must have a photo");
        }
    }

}
