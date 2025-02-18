package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    public AdminController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users",userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("new")
    public String createNewUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles",userService.getAllRoles());
        return "admin/new";
    }

    @PostMapping
    public String saveUser(@ModelAttribute @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles",userService.getAllRoles());
            return "admin/new";
        } else {
            try {
//                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userService.saveUser(user);
            } catch (RuntimeException e) {
                bindingResult.rejectValue("name", "",e.getMessage());
                model.addAttribute("roles",userService.getAllRoles());
                return "admin/new";
            }
            return "redirect:/admin/";
        }
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user",userService.getUserById(id));
        model.addAttribute("roles", userService.getAllRoles());
        return "admin/edit";
    }
    @PatchMapping("/update")
    public String updateUser(@RequestParam Integer id, @ModelAttribute @Valid User user,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            return "admin/edit";
        } else {
            userService.updateUser(id, user);
        }
        return "redirect:/admin/";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}
