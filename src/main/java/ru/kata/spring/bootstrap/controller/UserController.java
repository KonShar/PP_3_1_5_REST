package ru.kata.spring.bootstrap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.bootstrap.service.UserService;

import java.security.Principal;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String showUserInfo(Principal principal, Model model) {
        model.addAttribute(userService.getUserByName(principal.getName()));
        return "user/user";
    }


}
