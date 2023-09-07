package com.example.petshop;


import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableMongoRepositories
public class MongoDBTestContainerConfig {
    @Container
    public static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3")
            .withExposedPorts(27017);

    static {
        mongoDBContainer.start();
        var mappedPort = mongoDBContainer.getMappedPort(27017);
        System.setProperty("mongodb.container.port", String.valueOf(mappedPort));
    }

}
