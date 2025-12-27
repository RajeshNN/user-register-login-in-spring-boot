package com.usertemplate.userbackend.userController;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user; // Reference to your actual user object

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // This is where the magic happens:
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // You MUST return the list of authorities here.
        // Even if your 'User' object doesn't have a 'role' field

        // Returning empty collection. Change later as per use case.
        List<GrantedAuthority> authorities = Collections.emptyList();

        return authorities;
    }

    // The rest of the required UserDetails methods:
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public Date getLastLogout() {return user.getLastLogout(); }
}
