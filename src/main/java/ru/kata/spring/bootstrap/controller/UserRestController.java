package ru.kata.spring.bootstrap.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.bootstrap.entity.User;
import ru.kata.spring.bootstrap.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService)  {
        this.userService = userService;
    }

    @GetMapping
    public User getUser(Principal principal) {
        return userService.getUserByEmail(principal.getName());
    }

}
