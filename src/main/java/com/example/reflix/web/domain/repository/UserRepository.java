package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String username);
}
