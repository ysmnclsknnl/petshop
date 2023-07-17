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
    @JsonDeserialize(using = BinaryDeserializer.class)
    private Binary photo;

    public Pet(SuperPet superPet, MultipartFile photo) throws IOException {
        super(superPet.getName(), superPet.getDescription(), superPet.getAge(),superPet.getType(), false);
        if (!photo.isEmpty()) {
            byte[] photoBytes = photo.getBytes();
            String base64String = Base64.getEncoder().encodeToString(photoBytes);
            byte[] binaryData = Base64.getDecoder().decode(base64String);
            this.setPhoto(new Binary(binaryData));
        } else {
            throw new IllegalArgumentException("You should load a photo");
        }
    }

}
