package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entites.Role;
import ru.kata.spring.boot_security.demo.entites.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    List<Role> getAllRoles();

    void saveUser(User user);

    User getUserById(int id);

    User getUserByName(String name);

    void updateUser(int id, User user);

    void deleteUser(int id);
}
