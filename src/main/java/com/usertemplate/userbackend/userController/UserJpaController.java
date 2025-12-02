package com.usertemplate.userbackend.userController;

import com.usertemplate.userbackend.userController.User;
import com.usertemplate.userbackend.userController.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jpa/users")
public class UserJpaController {

    private final UserService userService;

    @Autowired
    public UserJpaController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);

        // Use Java 8 Optional pattern to return 200 OK or 404 Not Found
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {
        User savedUser = userService.saveUser(newUser);
        // Returns 201 Created status
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
