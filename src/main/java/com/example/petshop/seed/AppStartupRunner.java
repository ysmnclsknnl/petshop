package com.example.petshop.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppStartupRunner implements CommandLineRunner {
    private final PetDataSeeder petDataSeeder;

    // why a separate app runner?
    // How can I do it differently?
    // nvm i understood the CommandLineRunner interface wrongly, another way to dot his is to add a eventlistener for the application startup event or to use postconstruct, for example in the petDataSeeder.
    public AppStartupRunner(PetDataSeeder petDataSeeder) {
        this.petDataSeeder = petDataSeeder;
    }

    @Override
    public void run(String... args) {
        petDataSeeder.seedData();
    }
}
