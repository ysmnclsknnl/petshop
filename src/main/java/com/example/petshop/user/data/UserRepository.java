package com.example.petshop.user.data;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, ObjectId> {
   Optional<User> findByEmail(String email);
   boolean existsByEmail(String email);

   void deleteByEmail(String username);
}
