package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.Movie;
import com.example.reflix.web.dto.ContentsRecommendResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Movie findByContentsId(Long Id);

    @Query("select new com.example.reflix.web.dto.ContentsRecommendResponseDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) from Movie m where m.contentsId IN :longs")
    List<ContentsRecommendResponseDto> findAllBymovieId(Iterable<Long> longs);

    @Query("select m from Movie m where m.Name like :q")
    List<Movie> findByNameSearch(String q);

//    @Query
//    List<Movie> findAllLi
//    List<movie> findAllByContentsId(Iterable<Long> longs);
}
