package org.example.repositories;

import org.example.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private Map<Long, User> userMap;
    private static int counter;
    public UserRepositoryImpl() {
        this.userMap = new HashMap<>();
        counter = 1;
    }
    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User save(User user) {
        user.setId(counter++);
        userMap.put(user.getId(), user);
        return user;
    }
}
