package com.vehiclerental.vehicle_rental_system.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
}
