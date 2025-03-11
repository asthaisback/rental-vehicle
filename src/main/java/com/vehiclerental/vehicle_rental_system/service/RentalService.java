package com.vehiclerental.vehicle_rental_system.service;

import com.vehiclerental.vehicle_rental_system.dto.RentalHistoryDTO;
import com.vehiclerental.vehicle_rental_system.dto.RentalRequestDTO;
import com.vehiclerental.vehicle_rental_system.dto.RentalResponseDTO;
import com.vehiclerental.vehicle_rental_system.dto.RentalStatusDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RentalService {
    ResponseEntity<RentalResponseDTO> bookRental(RentalRequestDTO rentalRequestDTO);
    ResponseEntity<RentalStatusDTO> checkRentalStatus(Long vehicleId, Long userId);
    ResponseEntity<RentalStatusDTO> cancelRental(Long vehicleId, Long userId);
    ResponseEntity<List<RentalHistoryDTO>> getRentalHistory(Long userId);
}
