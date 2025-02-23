package ru.kata.spring.bootstrap.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.bootstrap.entity.User;
import ru.kata.spring.bootstrap.service.RoleService;
import ru.kata.spring.bootstrap.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/admin/")
public class AdminController {


    private final UserService userService;

    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(@ModelAttribute("user") User user,Principal principal, Model model) {
        model.addAttribute("admin",userService.getUserByEmail(principal.getName()));
        model.addAttribute("users",userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/users";
    }

    @GetMapping("/new")
    public String createNewUser(@ModelAttribute User user, Model model) {
        model.addAttribute("roles",roleService.getAllRoles());
        return "admin/new";
    }

    @PostMapping
    public String saveUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin/";
    }

    @GetMapping("/update/{id}")
    public String updateUser(@PathVariable Integer id, Model model) {
        model.addAttribute("user",userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/edit";
    }
    @PatchMapping("/update")
    public String updateUser(@RequestParam Integer id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin/";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Integer id, Model model) {
        model.addAttribute("user",userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/delete";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return "redirect:/admin/";
    }
}

