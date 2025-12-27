package com.usertemplate.userbackend.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    // You would inject the BCryptPasswordEncoder here typically

    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
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
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }



    @Transactional
    public Date setLogoutTime(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setLastLogout(new Date(System.currentTimeMillis()));
        userRepository.saveAndFlush(user);
        return user.getLastLogout();
    }


    // Save/Create a User (Transactional ensures database consistency)
    @Transactional
    public User saveUser(User user) {
        // Here is where you would hash the user.getPassword() before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
