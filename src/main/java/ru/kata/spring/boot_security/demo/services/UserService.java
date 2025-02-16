package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entites.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getAllUsers();

    void saveUser(User user);

    User getUserById(int id);

    void updateUser(int id, User user);

    void deleteUser(int id);
}
