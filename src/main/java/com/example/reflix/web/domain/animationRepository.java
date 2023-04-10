package com.example.reflix.web.domain;

import com.example.reflix.web.dto.recommendContentsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface animationRepository extends JpaRepository<animation,Long> {


    animation findByContentsId(Long Id);

    @Query("select new com.example.reflix.web.dto.recommendContentsDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) from animation m")
    List<recommendContentsDto> findAllByAniId(Iterable<Long> longs);
}
