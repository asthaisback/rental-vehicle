
package com.vehiclerental.vehicle_rental_system.model;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Collections;


@Data
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String role;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
    public boolean isAccountNonExpired() {
        return true;
    }
    public boolean isAccountNonLocked() {
        return true;
    }
    public boolean isCredentialsNonExpired() {
        return true;
    }
    public boolean isEnabled() {
        return true;
    }
}