package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.Movie;
import com.example.reflix.web.domain.Tvseris;
import com.example.reflix.web.dto.ContentsRecommendResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvRepository extends JpaRepository<Tvseris,Long> {


    Movie findByContentsId(Long Id);

    @Query("select new com.example.reflix.web.dto.ContentsRecommendResponseDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) from Tvseris m  where m.contentsId IN :longs and m.year > :startDate and m.year < :endDate")
    List<ContentsRecommendResponseDto> findAllByTvId(Iterable<Long> longs,String startDate,String endDate);


    @Query("select t from Tvseris t where t.Name like :q")
    List<Tvseris> findByNameSearch(String q);
}
