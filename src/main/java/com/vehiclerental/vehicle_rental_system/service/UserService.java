package com.vehiclerental.vehicle_rental_system.service;

import com.vehiclerental.vehicle_rental_system.dto.UserDTO;

public interface UserService {
    boolean registerUser(UserDTO userDTO);
    boolean loginUser(UserDTO userDTO);
}
