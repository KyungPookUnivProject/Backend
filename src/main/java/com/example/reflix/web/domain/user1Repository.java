package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface user1Repository extends JpaRepository<user1,Long> {

    Optional<user1> findByEmail(String username);
}
