package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface recomendContentsRepository extends JpaRepository<recomendContents,Long> {

    public void deleteByUserId(Long userId);
}
