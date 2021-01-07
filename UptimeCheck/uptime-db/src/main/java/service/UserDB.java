package service;

import domain.User;
import domain.WebsiteStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class UserDB {

    private static Map<String, User> users = new HashMap<>();

    public Optional<User> find(String name) {
        return Optional.ofNullable(users.get(name.toLowerCase()));
    }

    public List<User> findAll() {
        return users.values().stream().collect(Collectors.toList());
    }

    public void save(User user) { users.put(user.getName().toLowerCase(), user); }

}