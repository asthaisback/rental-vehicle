package com.vehiclerental.vehicle_rental_system.controller;

import com.vehiclerental.vehicle_rental_system.dto.*;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import com.vehiclerental.vehicle_rental_system.service.RentalService;
import com.vehiclerental.vehicle_rental_system.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    @Autowired
    private final VehicleService vehicleService;
    @Autowired
    private final RentalService rentalService;

    @GetMapping("/vehicles")
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicle();
    }

    @PostMapping("/rental/request")
    public ResponseEntity<RentalResponseDTO> bookRental(@RequestBody RentalRequestDTO rentalRequestDTO) {
        return rentalService.bookRental(rentalRequestDTO);
    }



    @GetMapping("/vehicles/available")
    public ResponseEntity<List<VehicleDTO>> getAllAvailableVehicles() {
        List<VehicleDTO> availableVehicles = vehicleService.getAllAvailableVehicles();
        return ResponseEntity.ok(availableVehicles);
    }

    @GetMapping("/rental/status")
    public ResponseEntity<RentalStatusDTO> checkRentalStatus(
            @RequestParam Long vehicleId,
            @RequestParam Long userId) {
        return rentalService.checkRentalStatus(vehicleId, userId);
    }



    @DeleteMapping("/cancel")
    public ResponseEntity<RentalStatusDTO> cancelRental(@RequestParam Long vehicleId, @RequestParam Long userId) {
        return rentalService.cancelRental(vehicleId, userId);
    }




    @GetMapping("/rental/history")
    public ResponseEntity<List<RentalHistoryDTO>> getRentalHistory(@RequestParam Long userId) {
        return rentalService.getRentalHistory(userId);
    }


    @GetMapping("/page/vehicles")
    public Page<VehicleDTO> getAllVehicles(Pageable pageable) {
        return vehicleService.getAllVehicles(pageable);
    }

    @GetMapping("/available")
    public List<VehicleDTO> getAvailableVehicles() {
        return vehicleService.getAvailableVehicles();
    }



    @GetMapping("/availableByDate")

//    public List<Vehicle> getAvailableVehicles(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
//        return vehicleService.getAvailableVehicles(startDate, endDate);
//    }
    public ResponseEntity<?> getAvailableVehicles(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDate today = LocalDate.now();
        if (startDate.isBefore(today) || endDate.isBefore(today)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No vehicles available for the selected dates.");
        }
        List<Vehicle> availableVehicles = vehicleService.getAvailableVehicles(startDate, endDate);

        if (availableVehicles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No vehicles available for the selected dates.");
        }

        return ResponseEntity.ok(availableVehicles);
    }

}

