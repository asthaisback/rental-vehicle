package com.vehiclerental.vehicle_rental_system.service;

import com.vehiclerental.vehicle_rental_system.model.Rental;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import com.vehiclerental.vehicle_rental_system.repository.RentalRepository;
import com.vehiclerental.vehicle_rental_system.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
public class RentalScheduler {
    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;

    // Run every hour (adjust the cron expression as needed)
    @Scheduled(cron = "0 0 * * * *") // Run at 00 minutes of every hour
    public void updateVehicleAvailability() {         // at every hr this method will run
        List<Rental> expiredRentals = rentalRepository.findByEndDateBeforeAndStatus(LocalDate.now(), "APPROVED");

        for (Rental rental : expiredRentals) {
            Vehicle vehicle = rental.getVehicle();
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);

            rental.setStatus("COMPLETED");
            rentalRepository.save(rental);
        }
    }
}
