package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//@EnableJpaRepositories
@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    public List<Review> findByContentName(String contentname);
    public List<Review> findAllByTvserisId(Long Id);
    public List<Review> findAllByAnimationId(Long Id);
    public List<Review> findAllByMovieId(Long Id);

}
