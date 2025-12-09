package com.usertemplate.userbackend.userController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @GetMapping("/home")
    public ResponseEntity<User> getLoggedInUser(@RequestBody Principal principal) {
        Optional<User> user = userService.findUserByUsername(principal.getName());
        if (user.isEmpty()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);

        // Use Java 8 Optional pattern to return 200 OK or 404 Not Found
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@ModelAttribute loginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        if(username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> user = userService.loginUser(username, password);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@ModelAttribute User newUser) {
        if(userService.findUserByUsername(newUser.getUsername()).isEmpty()){
            User savedUser = userService.saveUser(newUser);
            // Returns 201 Created status
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(newUser);
    }
}
