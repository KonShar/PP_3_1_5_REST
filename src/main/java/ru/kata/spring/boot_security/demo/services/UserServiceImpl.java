package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entites.Role;
import ru.kata.spring.boot_security.demo.entites.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        Optional<User> userFromBD = userRepository.findUserByName(user.getName());
        if (userFromBD.isPresent()) {
//            return false;
            throw new RuntimeException(String.format("User %s already exists", user.getUsername()));
        } else {
//            user.setPassword(new PasswordEncoder(user.getPassword()));
            userRepository.save(user);
//            return true;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(int id) {
        return null;
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findUserByName(name).get();
    }

    @Override
    public void updateUser(int id, User user) {

    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findUserByName(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        } else {
            return userOptional.get();
        }
    }
}
