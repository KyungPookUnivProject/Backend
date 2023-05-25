package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.ContentsJanre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentsJanreRepository extends JpaRepository<ContentsJanre,Long> {


    public ContentsJanre findByMovieId(Long Id);
    public ContentsJanre findByTvserisId(Long Id);
    public ContentsJanre findByAnimationId(Long Id);

}
