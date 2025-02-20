package ru.kata.spring.bootstrap.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.bootstrap.entity.Role;
import ru.kata.spring.bootstrap.entity.User;
import ru.kata.spring.bootstrap.repository.RoleRepository;
import ru.kata.spring.bootstrap.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public User saveUser(User user) {
        Optional<User> userFromBD = userRepository.findUserByEmail(user.getEmail());
        if (userFromBD.isPresent()) {
            throw new RuntimeException(String.format("User %s already exists", user.getUsername()));
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return userRepository.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String name) {
        return userRepository.findUserByEmail(name).get();
    }

    @Override
    public User updateUser(int id, User user) {
        Optional<User> updatedUser = userRepository.findById(id);
        user.setPassword(updatedUser.get().getPassword());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

}
