package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.contentsKeword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentsKeywordRepository extends JpaRepository<contentsKeword,Long> {
}
