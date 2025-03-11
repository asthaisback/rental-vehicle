package com.vehiclerental.vehicle_rental_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RentalResponseDTO {
    private Long id;
    private Long vehicleId;
    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String message;
}


