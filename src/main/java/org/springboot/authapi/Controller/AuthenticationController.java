package org.springboot.authapi.Controller;
import org.springboot.authapi.Dto.LoginUserDto;
import org.springboot.authapi.Dto.RegisterUserDto;
import org.springboot.authapi.Dto.userDto;

import org.springboot.authapi.Enities.User;
import org.springboot.authapi.Repository.UserRepository;
import org.springboot.authapi.Service.AuthenticationService;
import org.springboot.authapi.Service.JwtService;
import org.springboot.authapi.Service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class AuthenticationController {

    @Autowired private JwtService jwtService;

    @Autowired private AuthenticationService authenticationService;

    @Autowired private UserRepository userRepository;

    @Autowired private TokenBlacklistService tokenBlacklistService;


//    @PostMapping("/signup")
//    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
//        User registerUser=authenticationService.signup(registerUserDto);
//        return ResponseEntity.ok(registerUser);
//    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@RequestBody RegisterUserDto registerUserDto) {
        authenticationService.signup(registerUserDto);
        return ResponseEntity.ok("User registered successfully");
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
//        User authenticateUser = authenticationService.authenticate(loginUserDto);
//
//        String jwtToken=jwtService.generateToken(authenticateUser);
//        LoginResponse loginResponse= new LoginResponse().setToken(jwtToken).setFullName(authenticateUser.getFullName());
//        return ResponseEntity.ok(loginResponse);
//    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticateUser = authenticationService.authenticate(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticateUser);
        return ResponseEntity.ok(jwtToken);
    }


    @GetMapping("/user")
    public ResponseEntity<userDto> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());

        return user.map(u -> ResponseEntity.ok(new userDto(u.getEmail(), u.getFullName())))
                .orElseGet(() -> ResponseEntity.status(404).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Invalid token format");
        }

        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        tokenBlacklistService.blacklistToken(jwtToken);
        return ResponseEntity.ok("Logged out successfully");
    }

}
 

//--------------------------------------------------------12----------------------------------------------