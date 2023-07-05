package com.example.petshop.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {
    private String database;
    private String uri;

    // Getter and setter methods for database and uri
}
