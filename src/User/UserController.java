package com.JPA.JPA.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.management.RuntimeErrorException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with id %d";
    private static final String USER_NOT_FOUND_MESSAGE_WITH_NAME = "User not found with name %s";
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE
    @PostMapping
    public Users createUser(@RequestBody Users users) {
        return userService.saveUser(users);
    }

    // READ (all)
    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    // Read by name
    @GetMapping("/name")
    public Users getUser(@RequestParam String name){
        return userService.findByName(name)
                .orElseThrow(() -> new RuntimeException(
                        String.format(USER_NOT_FOUND_MESSAGE_WITH_NAME, name)
                ));
    }

    // READ (by id)
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException(String.format(USER_NOT_FOUND_MESSAGE, id)));
    }
    // READ (by email)
    @GetMapping("/{email}")
    public Users getUserByEmail(@PathVariable String email){
        return userService.findByEmail(email).orElseThrow(() -> new RuntimeException(
                "User not found with email"
        ));
    }

    // READ (by name)
    @GetMapping("/{name}")
    public Users getUserByName(@PathVariable String name){
        return userService.findByName(name).orElseThrow(()->
                new RuntimeException("User not found with name"));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users updatedUsers) {
        return userService.updateUser(id, updatedUsers);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (userService.isDatabaseEmpty())
            return userService.getEmptyDatabaseMessage(id);
        if (userService.getUserById(id).isEmpty())
            return String.format(USER_NOT_FOUND_MESSAGE, id);
        userService.deleteUser(id);
        return "User with id " + id + " deleted successfully!";
    }
}