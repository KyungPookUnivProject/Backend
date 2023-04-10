package com.example.reflix.web.domain;

import com.example.reflix.web.dto.recommendContentsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface movieRepository extends JpaRepository<movie,Long> {

    movie findByContentsId(Long Id);

    @Query("select new com.example.reflix.web.dto.recommendContentsDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) from movie m where m.contentsId IN :longs")
    List<recommendContentsDto> findAllBymovieId(Iterable<Long> longs);

//    List<movie> findAllByContentsId(Iterable<Long> longs);
}
