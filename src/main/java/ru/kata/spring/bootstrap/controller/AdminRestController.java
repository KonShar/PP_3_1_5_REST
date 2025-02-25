package ru.kata.spring.bootstrap.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.bootstrap.entity.Role;
import ru.kata.spring.bootstrap.entity.User;
import ru.kata.spring.bootstrap.service.RoleService;
import ru.kata.spring.bootstrap.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AdminRestController {

    private final UserService userService;

    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user")
    public User getUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/users")
    public User saveNewUser(@RequestBody User user) {
       return userService.saveUser(user);
    }

    @PatchMapping("/users/{id}")
    public User updateUser(@PathVariable Integer id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        // Возвращаем статус 200 (OK).
        return ResponseEntity.ok().build();
    }

}
