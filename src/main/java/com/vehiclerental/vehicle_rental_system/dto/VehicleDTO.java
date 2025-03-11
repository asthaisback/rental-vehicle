package com.vehiclerental.vehicle_rental_system.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VehicleDTO {
    private Long id;
    private String brand;
    private String model;
    private double rentalPrice;
    private boolean available;

}
