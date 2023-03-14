package com.example.reflix.web.domain;

import com.example.reflix.web.dto.contentsDetailResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface contentsRepository extends JpaRepository<contents,Long> {

    contentsDetailResponseDto findByContentsId(Long Id);
}
