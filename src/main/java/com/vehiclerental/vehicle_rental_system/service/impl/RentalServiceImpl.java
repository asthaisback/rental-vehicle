package com.vehiclerental.vehicle_rental_system.service.impl;

import com.vehiclerental.vehicle_rental_system.dto.*;
import com.vehiclerental.vehicle_rental_system.model.Rental;
import com.vehiclerental.vehicle_rental_system.model.User;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import com.vehiclerental.vehicle_rental_system.repository.RentalRepository;
import com.vehiclerental.vehicle_rental_system.repository.UserRepository;
import com.vehiclerental.vehicle_rental_system.repository.VehicleRepository;
import com.vehiclerental.vehicle_rental_system.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, List<VehicleDTO>> redisTemplate;

    @Override
    public ResponseEntity<RentalResponseDTO> bookRental(RentalRequestDTO rentalRequestDTO) {
        // Fetch the vehicle and user
        System.out.println("bsdsndks" +
                "sjdbsjdsk" +
                "                               " +
                "                          " +
                "                      fdb          ");
        System.out.println("Received request to book rental.");
        System.out.println("Vehicle ID: " + rentalRequestDTO.getVehicleId());
        System.out.println("User ID: " + rentalRequestDTO.getUserId());

        if (rentalRequestDTO.getVehicleId() == null) {
            throw new IllegalArgumentException("Vehicle ID must not be null!");
        }
        if (rentalRequestDTO.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null!");
        }

        System.out.println("bsdsndks" +
                "sjdbsjdsk" +
                "                               " +
                "                          " +
                "                                ");

        Vehicle vehicle = vehicleRepository.findById(rentalRequestDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        User user = userRepository.findById(rentalRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check the vehicle is already rented
        List<Rental> existingRentals = rentalRepository.findByVehicleId(rentalRequestDTO.getVehicleId());

        if (existingRentals.isEmpty()) {

            Rental rental = Rental.builder()
                    .vehicle(vehicle)
                    .user(user)
                    .startDate(rentalRequestDTO.getStartDate())
                    .endDate(rentalRequestDTO.getEndDate())
                    .status("APPROVED")
                    .build();

            Rental savedRental = rentalRepository.save(rental);

            vehicle.setAvailable(false);
            vehicleRepository.save(vehicle);

//            redisTemplate.delete(CacheConstant.AVAILABLE_VEHICLES_CACHE);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(RentalResponseDTO.builder()
                            .id(savedRental.getId())
                            .vehicleId(savedRental.getVehicle().getId())
                            .userId(savedRental.getUser().getId())
                            .startDate(savedRental.getStartDate())
                            .endDate(savedRental.getEndDate())
                            .status(savedRental.getStatus())
                            .message("Your vehicle is booked successfully")
                            .build());
        } else {
            // Check for overlapping dates
            boolean isOverlapping = existingRentals.stream()
                    .anyMatch(rental -> isDateRangeOverlapping(
                            rental.getStartDate(), rental.getEndDate(),
                            rentalRequestDTO.getStartDate(), rentalRequestDTO.getEndDate()
                    ));

            if (isOverlapping) {
                // Vehicle is already booked for the requested dates
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(RentalResponseDTO.builder()
                                .status("REJECTED")
                                .message("Vehicle is already booked for the selected dates")
                                .build());
            }  else {
                // Book the vehicle
                Rental rental = Rental.builder()
                        .vehicle(vehicle)
                        .user(user)
                        .startDate(rentalRequestDTO.getStartDate())
                        .endDate(rentalRequestDTO.getEndDate())
                        .status("APPROVED")
                        .build();

                Rental savedRental = rentalRepository.save(rental);


                vehicle.setAvailable(false);
                vehicleRepository.save(vehicle);

//                redisTemplate.delete(CacheConstant.AVAILABLE_VEHICLES_CACHE);

                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(RentalResponseDTO.builder()
                                .id(savedRental.getId())
                                .vehicleId(savedRental.getVehicle().getId())
                                .userId(savedRental.getUser().getId())
                                .startDate(savedRental.getStartDate())
                                .endDate(savedRental.getEndDate())
                                .status(savedRental.getStatus())
                                .message("Your vehicle is booked successfully")
                                .build());
            }
        }
    }


    //  check if two date ranges overlap
    private boolean isDateRangeOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }


    @Override
    public ResponseEntity<RentalStatusDTO> checkRentalStatus(Long vehicleId, Long userId) {
        // Find all rentals for the given vehicle and user
        List<Rental> rentals = rentalRepository.findAllByVehicleIdAndUserId(vehicleId, userId);

        if (rentals.isEmpty()) {
            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("You did not book this ride")
                    .build());
        }

        Rental latestRental = rentals.stream()
                .max(Comparator.comparing(Rental::getEndDate))
                .orElseThrow(() -> new RuntimeException("No rental found"));

        LocalDate currentDate = LocalDate.now();

        if (currentDate.isBefore(latestRental.getStartDate())) {
            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("Will be in progress")
                    .build());
        } else if (currentDate.isAfter(latestRental.getEndDate())) {
            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("Your ride is completed")
                    .build());
        } else {
            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("In Progress")
                    .build());
        }
    }





    @Override
    public ResponseEntity<RentalStatusDTO> cancelRental(Long vehicleId, Long userId) {
        // Find all rentals for the given vehicle and user
        List<Rental> rentals = rentalRepository.findAllByVehicleIdAndUserId(vehicleId, userId);

        if (rentals.isEmpty()) {
            // User never booked this rental
            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("You never booked this rental.")
                    .build());
        }

        // Get the latest rental (most recent endDate)
        Rental latestRental = rentals.stream()
                .max(Comparator.comparing(Rental::getEndDate))
                .orElseThrow(() -> new RuntimeException("No rental found"));

        LocalDate currentDate = LocalDate.now();

        if (currentDate.isBefore(latestRental.getStartDate())) {
            // Rental is upcoming, allow cancellation
            rentalRepository.delete(latestRental);  // Remove booking

            // Update vehicle availability
            Vehicle vehicle = vehicleRepository.findById(vehicleId)
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);

            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("Your rental has been successfully canceled.")
                    .build());
        } else if (currentDate.isAfter(latestRental.getEndDate()) ||
                (currentDate.isEqual(latestRental.getStartDate()) || currentDate.isAfter(latestRental.getStartDate()))) {
            // Ride is in progress or completed, cannot cancel
            return ResponseEntity.ok(RentalStatusDTO.builder()
                    .status("Cannot cancel now. The ride is already in progress or completed.")
                    .build());
        }

        return ResponseEntity.ok(RentalStatusDTO.builder()
                .status("Unexpected error. Please try again later.")
                .build());
    }







        @Override
        public ResponseEntity<List<RentalHistoryDTO>> getRentalHistory(Long userId) {

            List<Rental> rentals = rentalRepository.findByUserId(userId);


            List<RentalHistoryDTO> rentalHistory = rentals.stream()
                    .map(this::mapToRentalHistoryDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(rentalHistory);
        }

        private RentalHistoryDTO mapToRentalHistoryDTO(Rental rental) {
            return RentalHistoryDTO.builder()
                    .id(rental.getId())
                    .vehicleId(rental.getVehicle().getId())
                    .startDate(rental.getStartDate())
                    .endDate(rental.getEndDate())
                    .status(rental.getStatus())
                    .build();
        }

}