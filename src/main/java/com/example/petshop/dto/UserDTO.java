package com.example.petshop.dto;

import com.example.petshop.collection.User;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Builder
@Data
public class UserDTO {
    private ObjectId id;
    private String username;

    public static UserDTO from(User user) {
        return builder()
                .id(user.getUserId())
                .username(user.getEmail())
                .build();
    }
}