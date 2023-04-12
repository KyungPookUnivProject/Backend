package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.ContentsLikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentsLikeListRepository extends JpaRepository<ContentsLikeList,Long> {
    
        Optional<ContentsLikeList> findByContentIdAndUser(Long contentId, Long userId);
        Long countByContentId(Long contentId);
    }