package com.JPA.JPA.User;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String EMPTY_DATABASE_MESSAGE = "Empty database - no user with id %d will be found";

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public Users saveUser(Users users) {
        return userRepository.save(users);
    }

    // READ (all)
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<Users> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<Users> findByName(String name){
        return userRepository.findByName(name);
    }

    // READ (by id)
    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // UPDATE
    public Users updateUser(Long id, Users updatedUsers) {
        return userRepository.findById(id)
                .map(users -> {
                    users.setName(updatedUsers.getName());
                    users.setEmail(updatedUsers.getEmail());
                    return userRepository.save(users);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    // DELETE
    public void deleteUser(Long id) {
        System.out.println(userRepository.findAll().size());
        userRepository.deleteById(id);
    }

    public boolean isDatabaseEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public String getEmptyDatabaseMessage(Long id) {
        return String.format(EMPTY_DATABASE_MESSAGE, id);
    }

}