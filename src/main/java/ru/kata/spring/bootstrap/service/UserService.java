package ru.kata.spring.bootstrap.service;

import ru.kata.spring.bootstrap.entity.Role;
import ru.kata.spring.bootstrap.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    List<Role> getAllRoles();

    void saveUser(User user);

    User getUserById(int id);

    User getUserByName(String name);

    void updateUser(int id, User user);

    void deleteUser(int id);
}
