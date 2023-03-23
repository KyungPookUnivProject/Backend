package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface reviewRepository extends JpaRepository<review,Long> {

    public List<review> findByContentName(String contentname);

}
