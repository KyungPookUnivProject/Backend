package com.example.reflix.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@EnableJpaRepositories
@Repository
public interface reviewRepository extends JpaRepository<review,Long> {

    public List<review> findByContentName(String contentname);
    public List<review> findAllByTvserisId(Long Id);
    public List<review> findAllByAnimationId(Long Id);
    public List<review> findAllByMovieId(Long Id);

}
