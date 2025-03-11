package com.vehiclerental.vehicle_rental_system.dto;

import lombok.Data;

import java.time.LocalDate;


@Data
public class RentalRequestDTO {
    private Long vehicleId;
    private Long userId;  // The ID of the user renting the vehicle.
    private LocalDate startDate;
    private LocalDate endDate;
}

//This DTO represents a request to create a rental
// This DTO is used when a user submits a request to rent a vehicle