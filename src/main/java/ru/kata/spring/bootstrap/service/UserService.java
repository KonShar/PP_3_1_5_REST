package ru.kata.spring.bootstrap.service;

import ru.kata.spring.bootstrap.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User saveUser(User user);

    User getUserById(int id);

    User getUserByEmail(String email);

    User updateUser(int id, User user);

    User deleteUser(int id);
}
