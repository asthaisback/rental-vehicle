package com.vehiclerental.vehicle_rental_system.controller;


import com.vehiclerental.vehicle_rental_system.dto.RentalResponseDTO;
import com.vehiclerental.vehicle_rental_system.dto.VehicleDTO;
import com.vehiclerental.vehicle_rental_system.service.RentalService;
import com.vehiclerental.vehicle_rental_system.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final VehicleService vehicleService;
    @PostMapping("/vehicles")
    public VehicleDTO addVehicle(@RequestBody VehicleDTO vehicleDTO) {



            log.info("wegdbwiudkjewdvewhgd" );



        return vehicleService.addVehicle(vehicleDTO);
    }


    @GetMapping("/vehicles")
    public List<VehicleDTO> getAllVehicles(){

        return vehicleService.getAllVehicle();
    }


    @DeleteMapping("/vehicles/{id}")
    public void deleteVehicle(@PathVariable Long id) {

        vehicleService.deleteVehicle(id);
    }


    @PutMapping("update/vehicles/{id}")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id,@RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @GetMapping("/vehicles/available")
    public ResponseEntity<List<VehicleDTO>> getAllAvailableVehicles() {
        List<VehicleDTO> availableVehicles = vehicleService.getAllAvailableVehicles();
        return ResponseEntity.ok(availableVehicles);
    }


}
