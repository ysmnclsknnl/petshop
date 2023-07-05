package com.example.petshop.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {
    private final PetDataSeeder petDataSeeder;

    public AppStartupRunner(PetDataSeeder petDataSeeder) {
        this.petDataSeeder = petDataSeeder;
    }

    @Override
    public void run(String... args) {
        petDataSeeder.seedData();
    }
}
