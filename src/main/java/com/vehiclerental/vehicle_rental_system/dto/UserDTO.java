package com.vehiclerental.vehicle_rental_system.dto;


import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String role;
}

// This DTO is used for user-related operations, such as:
//Registering a new user
// Logging in a user