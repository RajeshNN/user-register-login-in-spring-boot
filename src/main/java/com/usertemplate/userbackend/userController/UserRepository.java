package com.usertemplate.userbackend.userController;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // You automatically inherit methods like save(), findAll(), findById(), deleteById()

    // You can define custom methods, and Spring will implement them for you
//    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
//    User save(User user);
}
