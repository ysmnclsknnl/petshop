package com.example.petshop.seed;

import com.example.petshop.Type;
import com.example.petshop.collection.Pet;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.IntStream;

@Component
public class PetDataSeeder {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PetDataSeeder(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void seedData() {
        IntStream.range(0, 1)
                .mapToObj(i -> {
                    Pet pet = new Pet();
                    pet.setId(new ObjectId());
                    pet.setName("Pet " + i);
                    pet.setDescription("Description for Pet " + i);
                    pet.setAge(i);
                    pet.setType(Type.DOG);
                    pet.setAdopted(i % 2 == 0);
                    pet.setPhoto(new Binary(generateFakePhoto()));
                    return pet;
                })
                .forEach(mongoTemplate::save);
    }

    private byte[] generateFakePhoto() {
        int maxSize = 100 * 1024;
        byte[] photoData = new byte[maxSize];

        new Random().nextBytes(photoData);

        return photoData;
    }
}

