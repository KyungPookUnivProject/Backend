package com.example.reflix.web.domain;

import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface contentLikeListRepository extends JpaRepository<contentLikeList,Long> {

    Optional<contentLikeList> findByContentIdAndUser(Long contentId, Long userId);
    Long countByContentId(Long contentId);

}
