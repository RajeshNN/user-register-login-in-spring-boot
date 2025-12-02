package com.usertemplate.userbackend.userController;

import com.usertemplate.userbackend.userController.User;
import com.usertemplate.userbackend.userController.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    // You would inject the BCryptPasswordEncoder here typically

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Read All
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Read One by ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Save/Create a User (Transactional ensures database consistency)
    @Transactional
    public User saveUser(User user) {
        // Here is where you would hash the user.getPassword() before saving
        return userRepository.save(user);
    }
}
