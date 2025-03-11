package com.vehiclerental.vehicle_rental_system.repository;
import com.vehiclerental.vehicle_rental_system.model.Rental;
import com.vehiclerental.vehicle_rental_system.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental,Long> {
    List<Rental> findByVehicleId(Long vehicleId);
    List<Rental> findByEndDateBeforeAndStatus(LocalDate endDate, String status);
    List<Rental> findAllByVehicleIdAndUserId(Long vehicleId, Long userId);

    List<Rental> findByUserId(Long userId);


    @Query("SELECT r.vehicle FROM Rental r " +
            "WHERE (r.startDate <= :endDate AND r.endDate >= :startDate) " +
            "AND r.status = 'APPROVED'")
    List<Vehicle> findRentedVehicles(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

