package com.vehiclerental.vehicle_rental_system.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "vehicle_id")

    private Vehicle vehicle;

//    ek user ke paas kafi rentals ho skte h pr ek rental sirf ek user ka hoga
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;   // app, rej


}
