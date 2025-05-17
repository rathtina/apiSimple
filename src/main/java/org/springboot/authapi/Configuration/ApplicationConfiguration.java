package org.springboot.authapi.Configuration;

import org.springboot.authapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {

    @Autowired private  UserRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }
}

//UserDetailsService is used by Spring Security to load user data.
//authenticationProvider() → Uses DaoAuthenticationProvider to fetch users from the database.
//setUserDetailsService(userDetailsService()) → It tells Spring Security how to fetch user details (usually from a database).
//setPasswordEncoder(passwordEncoder()) → It ensures passwords are correctly hashed and matched (e.g., using BCrypt).
//Use @Bean PasswordEncoder → Ensures password hashing (e.g., BCrypt).
//Register it in SecurityFilterChain to enable authentication
//AuthenticationConfiguration is a Spring Boot helper class that manages authentication settings.

//----------How JWT Authentication Works------------
//User logs in with a username & password.
//Server verifies the credentials using AuthenticationManager.
//If authentication is successful, the server generates a JWT and returns it to the client.
//Client stores the JWT (e.g., in local storage or a cookie).
//For subsequent requests, the client sends the JWT in the Authorization header.
//Server verifies the JWT and grants or denies access.

//-------------------------AuthenticationManager-------------------------------
//How AuthenticationManager Works
//It takes an authentication request (UsernamePasswordAuthenticationToken).
//It passes the request to an AuthenticationProvider (e.g., DaoAuthenticationProvider).
//The AuthenticationProvider calls UserDetailsService to fetch user details.
//It compares the provided password with the stored password.
//If authentication is successful, it returns an authenticated Authentication object.
//If authentication fails, it throws an exception.

//--------------------authenticationManager.authenticate(.....)-----------------------------
//1-verifies the username and password.
//2- If valid, a JWT token is generated and returned.
//3-If invalid, an error is returned
//A helper class in Spring Boot is a utility class that contains reusable methods to simplify common tasks.

//-------------------------AuthenticationConfiguration----------------------------
//✅Retrieves the pre-configured AuthenticationManager.
//✅ Helps in injecting AuthenticationManager as a bean.
//✅ Allows custom authentication providers (e.g., DaoAuthenticationProvider).
//✅ Simplifies authentication setup in Spring Boot without requiring custom configuration.
// It helps retrieve AuthenticationManager that includes all registered authentication providers (e.g., DaoAuthenticationProvider).

//------------------------------------4-----------------------------------------------