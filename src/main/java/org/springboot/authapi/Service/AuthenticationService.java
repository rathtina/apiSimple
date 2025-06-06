package org.springboot.authapi.Service;

import org.springboot.authapi.Dto.LoginUserDto;
import org.springboot.authapi.Dto.RegisterUserDto;
import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired private UserRepository userRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired private AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto input){
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                input.getEmail(),
                input.getPassword()
        ));
        return userRepository.findByEmail(input.getEmail()).orElseThrow();
    }
}

//---------------------------------------------11-------------------------------------------