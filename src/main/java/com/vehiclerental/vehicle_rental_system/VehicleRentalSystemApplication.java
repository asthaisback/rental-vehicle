package com.vehiclerental.vehicle_rental_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
public class VehicleRentalSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleRentalSystemApplication.class, args);
	}

}
