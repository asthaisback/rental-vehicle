package com.vehiclerental.vehicle_rental_system.service.impl;

import com.vehiclerental.vehicle_rental_system.dto.VehicleDTO;
import com.vehiclerental.vehicle_rental_system.model.Rental;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import com.vehiclerental.vehicle_rental_system.repository.RentalRepository;
import com.vehiclerental.vehicle_rental_system.repository.VehicleRepository;
import com.vehiclerental.vehicle_rental_system.service.VehicleService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    @Autowired
    RentalRepository rentalRepository;

    private final RedisTemplate<String, List<VehicleDTO>> redisTemplate;

    private static final String CACHE_KEY = "availableVehicles";
    @Override
    public List<VehicleDTO> getAllVehicle() {
        return vehicleRepository.findAll().stream()

//              Converts each Vehicle object into a VehicleDTO object.
                .map(this::convertToDTO)

                .collect(Collectors.toList());
    }


    @Override
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle=vehicleRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Vehicle not found"));
        return convertToDTO(vehicle);
    }

    @Override
    public VehicleDTO addVehicle(VehicleDTO vehicleDTO) {
        Vehicle vehicle=new Vehicle();
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setRentalPrice(vehicleDTO.getRentalPrice());
        vehicle.setAvailable(vehicleDTO.isAvailable());
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }

    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // Update fields from DTO to entity
        existingVehicle.setBrand(vehicleDTO.getBrand());
        existingVehicle.setModel(vehicleDTO.getModel());
        existingVehicle.setRentalPrice(vehicleDTO.getRentalPrice());
        existingVehicle.setAvailable(vehicleDTO.isAvailable());

        Vehicle updatedVehicle = vehicleRepository.save(existingVehicle);
        return convertToDTO(updatedVehicle);
    }



    public List<VehicleDTO> getAllAvailableVehicles() {
        List<Vehicle> availableVehicles = vehicleRepository.findByAvailable(true);

        return availableVehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }





     // Inject RentalRepository
         // Initialize the rentalRepository

    @Override
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Vehicle not found with id: " + id);
        }
        List<Rental> activeRentals = rentalRepository.findByVehicleId(id);
        if (!activeRentals.isEmpty()) {
            // Check if any rental has a startDate in the future
            Rental activeRental = activeRentals.stream()
                    .filter(rental -> rental.getStartDate().isAfter(LocalDate.now()))
                    .findFirst()
                    .orElse(null);

            if (activeRental != null) {
                throw new RuntimeException("Rental can't be deleted, as the rental will start after the current date.");
            } else {
                vehicleRepository.deleteById(id);
            }
        } else {
            vehicleRepository.deleteById(id);
        }
    }


//    redis


    @Override
    public List<VehicleDTO> getAvailableVehicles() {
        //Check Redis cache
        List<VehicleDTO> cachedVehicles = redisTemplate.opsForValue().get(CACHE_KEY);

        if (cachedVehicles != null) {
            System.out.println("Fetch from redis cache.............");
            return cachedVehicles;
        }

        System.out.println("Fetch from db...............");

        List<Vehicle> availableVehicles = vehicleRepository.findByAvailable(true);
        List<VehicleDTO> vehicleDTOs = availableVehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        //Store data in Redis cache (cache for 10 minutes)
        redisTemplate.opsForValue().set(CACHE_KEY, vehicleDTOs, 10, TimeUnit.MINUTES);

        return vehicleDTOs;
    }
//for page
    public Page<VehicleDTO> getAllVehicles(Pageable pageable) {
//                                                   .map(vehicle->convertToDTO(vehicle))
        return vehicleRepository.findAll(pageable).map(this::convertToDTO);
    }


//    private VehicleDTO mapToVehicleDTO(Vehicle vehicle) {
//        return VehicleDTO.builder()
//                .id(vehicle.getId())
//                .brand(vehicle.getBrand())
//                .model(vehicle.getModel())
//                .rentalPrice(vehicle.getRentalPrice())
//                .available(vehicle.isAvailable())
//                .build();
//    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setBrand(vehicle.getBrand());
        dto.setModel(vehicle.getModel());
        dto.setRentalPrice(vehicle.getRentalPrice());
        dto.setAvailable(vehicle.isAvailable());
        return dto;
    }


    @Override
    public List<Vehicle> getAvailableVehicles(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();

        List<Vehicle> allAvailableVehicles = vehicleRepository.findByAvailableTrue();

        List<Vehicle> rentedVehicles = rentalRepository.findRentedVehicles(startDate, endDate);

        return allAvailableVehicles.stream()
                .filter(vehicle -> !rentedVehicles.contains(vehicle))
                .collect(Collectors.toList());
    }
}
