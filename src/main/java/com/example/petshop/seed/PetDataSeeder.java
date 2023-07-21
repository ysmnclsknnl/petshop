package com.example.petshop.seed;

import com.example.petshop.dto.Type;
import com.example.petshop.collection.Pet;
import com.example.petshop.repository.PetRepository;
import net.datafaker.Faker;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
public class PetDataSeeder {
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
         return new Pet(
                faker.cat().name(),
                "Lovely pet. Enjoys playing with its owner.",
                faker.number().numberBetween(0, 10),
                Type.CAT,
                faker.bool().bool(),
                new Binary(faker.photography().camera().getBytes())
         );
    }
}

