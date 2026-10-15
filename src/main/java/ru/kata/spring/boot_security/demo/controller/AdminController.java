package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {

        addCommonAttributes(model);

        model.addAttribute("activeTab", "usersTable");
        model.addAttribute("newUser", new User());

        return "admin";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam String name, @RequestParam String lastName, @RequestParam Integer age, @RequestParam String email, @RequestParam String password, @RequestParam(required = false) List<Long> roles) {

        userService.createUser(name, lastName, email, password, age, roles);

        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id, @RequestParam String name, @RequestParam String lastName, @RequestParam Integer age, @RequestParam String email, @RequestParam(required = false) String password, @RequestParam(required = false) List<Long> roles) {

        userService.updateUser(id, name, lastName, age, email, password, roles);

        return "redirect:/admin";
        }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {

        userService.deleteUser(id);

        return "redirect:/admin";
    }

    private void addCommonAttributes(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User authUser = (User) authentication.getPrincipal();

        model.addAttribute("authUser", authUser);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.findAll());
    }
}