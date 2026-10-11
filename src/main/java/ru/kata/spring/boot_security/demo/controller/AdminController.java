package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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

        User user = new User(name, lastName, email, password, age);

        user.setRoles(getRoles(roles));

        userService.saveUserWithEncryptedPassword(user);

        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id, @RequestParam String name, @RequestParam String lastName, @RequestParam Integer age, @RequestParam String email, @RequestParam String password, @RequestParam(required = false) List<Long> roles) {

        User user = userService.getUserById(id);

        if (user == null) {
            return "redirect:/admin";
        }

        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);

        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        user.setRoles(getRoles(roles));

        user.setUsername(email);

        userService.updateUser(user);

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

    private Set<Role> getRoles(List<Long> roleIds) {
        Set<Role> roles = new HashSet<>();
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                Role role = roleService.findById(roleId);
                if (role != null) {
                    roles.add(role);
                }
            }
        }
        return roles;
    }
}