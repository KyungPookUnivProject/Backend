package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.RecomendContents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendContentsRepository extends JpaRepository<RecomendContents,Long> {

    public void deleteByUserId(Long userId);
}
