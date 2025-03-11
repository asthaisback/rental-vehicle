package com.vehiclerental.vehicle_rental_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalHistoryDTO {
    private Long id;
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
