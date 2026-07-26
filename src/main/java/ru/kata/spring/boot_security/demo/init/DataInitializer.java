package ru.kata.spring.boot_security.demo.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            roleUser = new Role("ROLE_USER");
            roleRepository.save(roleUser);
            System.out.println("ROLE_USER created");
        }

        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
        if (roleAdmin == null) {
            roleAdmin = new Role("ROLE_ADMIN");
            roleRepository.save(roleAdmin);
            System.out.println("ROLE_ADMIN created");
        }

        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleAdmin);
            adminRoles.add(roleUser);

            admin = new User("Admin", "Adminov", "admin@mail.ru", "admin", passwordEncoder.encode("admin"));
            admin.setRoles(adminRoles);
            userRepository.save(admin);

            System.out.println("Admin created : admin / admin");
        } else {
            System.out.println("Admin already exists");
        }
    }
}
