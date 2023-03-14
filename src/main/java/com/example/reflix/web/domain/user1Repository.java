package com.example.reflix.web.domain;

import com.example.reflix.config.auth.userPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface user1Repository extends JpaRepository<user,Long> {

    Optional<user> findByEmail(String username);
}
