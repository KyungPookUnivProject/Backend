package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.Movie;
import com.example.reflix.web.dto.ContentsRecommendResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie,Long> {

    Optional<Movie> findByContentsId(Long Id);

//    @Query("select new com.example.reflix.web.dto.ContentsRecommendResponseDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) " +
//            "from Movie m where m.contentsId IN :longs")
//    List<ContentsRecommendResponseDto> findAllBymovieId(Iterable<Long> longs);

    @Query("select new com.example.reflix.web.dto.ContentsRecommendResponseDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) " +
            "from Movie m where m.contentsId IN :longs and m.year > :startDate and m.year < :endDate")
    List<ContentsRecommendResponseDto> findAllBymovieId(Iterable<Long> longs,String startDate,String endDate);

    @Query("select m from Movie m where m.Name like :q")
    List<Movie> findByNameSearch(String q);

    @Query("select m.contentsId from Movie m where m.contentsId>21000")
    List<Long> findAllId();

//    @Query(value = "SET FOREIGN_KEY_CHECKS = 0;",nativeQuery = true)
//    void setKey();
//    List<Movie> findAllLi
//    List<movie> findAllByContentsId(Iterable<Long> longs);
}
