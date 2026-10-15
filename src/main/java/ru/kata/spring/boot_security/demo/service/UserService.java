package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void saveUser(User user);
    void saveUserWithEncryptedPassword(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);

    void createUser(String name, String lastName, String email, String password, Integer age, List<Long> roleIds);

    void updateUser(Long id, String name, String lastName, Integer age, String email, String password, List<Long> roleIds);
}
