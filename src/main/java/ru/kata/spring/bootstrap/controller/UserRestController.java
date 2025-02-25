package ru.kata.spring.bootstrap.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.bootstrap.entity.Role;
import ru.kata.spring.bootstrap.entity.User;
import ru.kata.spring.bootstrap.service.RoleService;
import ru.kata.spring.bootstrap.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserRestController {

    private UserService userService;

    private RoleService roleService;

    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public User getUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }
}
