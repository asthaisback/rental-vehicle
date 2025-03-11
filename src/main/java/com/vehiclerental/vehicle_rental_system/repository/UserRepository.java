package com.vehiclerental.vehicle_rental_system.repository;

import com.vehiclerental.vehicle_rental_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
                                      //  entity,primary key column
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}

// optional class is introduce in java8 and it is basically use to handle null point exception
// Returns an Optional<User>, which may contain the user or be empty if no user is found
// null check avoid krta h