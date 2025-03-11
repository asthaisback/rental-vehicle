package com.vehiclerental.vehicle_rental_system.repository;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//This repository allows us to:
//Perform CRUD operations on the Vehicle table (e.g., save, update, delete, find by ID).
//Find all available vehicles using the findByAvailableTrue() method.

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
   List<Vehicle> findByAvailableTrue();
    List<Vehicle> findByAvailable(boolean available);
    Page<Vehicle> findAll(Pageable pageable);
}
