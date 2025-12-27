package com.usertemplate.userbackend.userController;


import com.usertemplate.userbackend.security.jwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/api/jpa/users")
public class UserJpaController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final jwtUtil jwtUtil;


    public UserJpaController(UserService userService, jwtUtil jwtUtil,  AuthenticationManager authenticationManager) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/home")
    public ResponseEntity<User> getLoggedInUser(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userService.findUserByUsername(userDetails.getUsername());
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
    public ResponseEntity<Map<String, String>> login(@RequestBody loginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        if(username.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Authentication unauthenticated =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authenticationResponse =
                authenticationManager.authenticate(unauthenticated);
        if (authenticationResponse.isAuthenticated()) {
            String token = jwtUtil.generateToken(username);
            // Set context
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
            // Return token to client
            return ResponseEntity.ok(Map.of("access_token", token, "token_type", "Bearer"));
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(Authentication authentication) {
        if(authentication != null &&  authentication.isAuthenticated()) {
            Date logoutTime = userService.setLogoutTime(authentication.getName());
            return ResponseEntity.ok(Map.of("logout_time", logoutTime.toString()));
        }
        else return  ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@ModelAttribute User newUser) {
        if(userService.findUserByUsername(newUser.getUsername()).isEmpty()){
            newUser.setLastLogout(new Date(System.currentTimeMillis()));
            User savedUser = userService.saveUser(newUser);
            // Returns 201 Created status
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(newUser);
    }
}
