package com.vehiclerental.vehicle_rental_system.controller;

//import com.vehiclerental.vehicle_rental_system.dto.UserDTO;
//import com.vehiclerental.vehicle_rental_system.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//    @Autowired
//    private final UserService userService;
//
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
//        boolean isRegister=userService.registerUser(userDTO);
//        if(isRegister){
//            return ResponseEntity.ok("REGISTER SUCCESSFULLY :)");
//        }else {
//            return ResponseEntity.status(409).body("YOUR ACCOUNT IS ALREADY EXIST :)");
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
//        boolean isLogin=userService.loginUser(userDTO);
//        if(isLogin){
//            return ResponseEntity.ok("SUCCESSFULLY LOGIN:)");
//        }else
//            return ResponseEntity.status(401).body("WRONG USERNAME OR PASSWORD :(");
//    }
//}

import com.vehiclerental.vehicle_rental_system.dto.AuthenticationRequest;
import com.vehiclerental.vehicle_rental_system.dto.AuthenticationResponse;
import com.vehiclerental.vehicle_rental_system.dto.UserDTO;
import com.vehiclerental.vehicle_rental_system.jwt.JwtUtil;
import com.vehiclerental.vehicle_rental_system.model.User;
import com.vehiclerental.vehicle_rental_system.repository.UserRepository;
import com.vehiclerental.vehicle_rental_system.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully :)");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}

