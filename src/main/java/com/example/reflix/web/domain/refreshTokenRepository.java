package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface refreshTokenRepository extends JpaRepository<refreshToken,Long> {
    Optional<refreshToken> findByUserId(String userId);

}
