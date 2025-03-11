package com.vehiclerental.vehicle_rental_system.service.impl;

import com.vehiclerental.vehicle_rental_system.dto.UserDTO;
import com.vehiclerental.vehicle_rental_system.model.User;
import com.vehiclerental.vehicle_rental_system.repository.UserRepository;
import com.vehiclerental.vehicle_rental_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public boolean registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        return true;
    }


    @Override
    public boolean loginUser(UserDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            return true;
        } else {
            return false;
        }
    }
}
