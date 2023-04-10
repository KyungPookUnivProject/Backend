package com.example.reflix.web.domain;

import com.example.reflix.web.dto.recommendContentsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvRepository extends JpaRepository<Tvseris,Long> {


    movie findByContentsId(Long Id);

    @Query("select new com.example.reflix.web.dto.recommendContentsDto(m.contentsId,m.Name,m.ImageUrl,m.contentsCategory,0) from Tvseris m")
    List<recommendContentsDto> findAllByTvId(Iterable<Long> longs);
}
