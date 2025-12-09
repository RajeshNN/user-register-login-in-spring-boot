package com.usertemplate.userbackend.userController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
// You might also have @EnableWebSecurity here if this is your main security config
public class securityConfiguration {

    /**
     * Defines the PasswordEncoder bean for the entire application.
     * We use BCrypt as a standard, secure default.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // This is the implementation Spring will use everywhere
        // the 'PasswordEncoder' interface is injected.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // List your public URLs here
                                .requestMatchers("/**", "/login", "/register").permitAll()
                                // Allow access to static resources (CSS, JS, Images)
//                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                // All other requests require authentication
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/api/jpa/users/login"))
                .logout(Customizer.withDefaults())
        ; // Use default logout behavior


        return http.build();
    }
}

