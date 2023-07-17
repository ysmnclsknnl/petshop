package com.example.petshop.collection;

import com.example.petshop.User.Role;
import com.example.petshop.serializer.ObjectIdSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    @JsonSerialize(using = ObjectIdSerializer.class)
    private ObjectId userId;
    private String name;
    private String email;
    private String password;
    private Role userType;

    public User(String name, String email, String password, Role userType) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.userType = userType;
    }
}
