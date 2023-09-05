package com.example.petshop.pet.data;

import net.datafaker.Faker;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class PetDataSeeder implements CommandLineRunner {
    private final PetRepository petRepository;

    @Autowired
    public PetDataSeeder(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public void seedData() {
        Faker faker = new Faker();
        List<Pet> pets = IntStream.range(0, 5)
                .mapToObj(i -> createPet(faker))
                .toList();

        petRepository.saveAll(pets);
    }
    private Pet createPet(Faker faker) {
        List<String> imageUrls = List.of(
                "https://i0.wp.com/deepgreenpermaculture.com/wp-content/uploads/2023/06/cat-relaxing-2000px.jpeg?fit=2000%2C1332&ssl=1",
                "https://p1.pxfuel.com/preview/159/551/746/cat-baby-baby-cat-kitten-cat-cute-sweet.jpg",
                "https://p0.pxfuel.com/preview/385/15/639/cute-mammal-cat-portrait.jpg"
        );
        ObjectId petId = new ObjectId() ;
        String petName = faker.cat().name();
        String description = faker.cat().breed();
        Integer age = faker.number().numberBetween(0, 10);
        final Type type = Type.CAT;
        Boolean adopted = faker.bool().bool();
        int randomIndex = faker.number().numberBetween(0, imageUrls.size());
        String photoLink = imageUrls.get(randomIndex);

        return new Pet(petId, petName, description, age, type, adopted, photoLink);
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
    }
}

