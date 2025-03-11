package com.vehiclerental.vehicle_rental_system.service;

import com.vehiclerental.vehicle_rental_system.dto.VehicleDTO;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface VehicleService {
    List<VehicleDTO> getAllVehicle();
    VehicleDTO getVehicleById(Long id);

    VehicleDTO addVehicle(VehicleDTO vehicleDTO);


    VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO);

    List<VehicleDTO> getAvailableVehicles();



    List<VehicleDTO> getAllAvailableVehicles();


    void deleteVehicle(Long id);

    Page<VehicleDTO> getAllVehicles(Pageable pageable); // Add pagination

    List<Vehicle> getAvailableVehicles(LocalDate startDate, LocalDate endDate);
}
