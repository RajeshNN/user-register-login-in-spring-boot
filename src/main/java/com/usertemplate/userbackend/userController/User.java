package com.usertemplate.userbackend.userController;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users") // Maps to the "users" table created earlier
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Uses auto-increment from DB (like SERIAL)
    private Long id;
    private String username;
    private String email;
    private String password; // Should store a hashed password

    // Getters and Setters are required by JPA/Hibernate
    // (Omitted for brevity in this snippet)
}
