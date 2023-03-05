package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface user1Repository extends JpaRepository<user,Long> {

    Optional<user> findByEmail(String username);
}
