package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface contentsRepository extends JpaRepository<contents,Long> {

    contents findByContentsId(Long Id);
//    contentsDetailResponseDto findByContentsId(Long Id);
}
