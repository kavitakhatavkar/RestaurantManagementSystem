package org.example.repositories;

import org.example.models.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(long id);
    User save(User user);
}
