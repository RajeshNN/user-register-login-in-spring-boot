package com.usertemplate.userbackend.security;

import com.usertemplate.userbackend.userController.CustomUserDetails;
import com.usertemplate.userbackend.userController.User;
import com.usertemplate.userbackend.userController.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository; // Your Spring Data JPA repository for User

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from the database
        Optional<User> user = userRepository.findByUsername(username);

        // Convert your custom User entity into Spring Security's UserDetails object
        if(user.isEmpty()) throw new UsernameNotFoundException(username);
        return new CustomUserDetails(user.get());
    }
}