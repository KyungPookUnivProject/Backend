package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.Animation;
import com.example.reflix.web.dto.ContentsRecommendResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimationRepository extends JpaRepository<Animation,Long> {


    Animation findByContentsId(Long Id);

    @Query("select new com.example.reflix.web.dto.ContentsRecommendResponseDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) from Animation m  where m.contentsId IN :longs and m.year > :startDate and m.year < :endDate")
    List<ContentsRecommendResponseDto> findAllByAniId(Iterable<Long> longs,String startDate,String endDate);

    @Query("select a from Animation a where a.Name like :q")
    List<Animation> findByNameSearch(String q);
}
