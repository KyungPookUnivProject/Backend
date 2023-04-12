package com.example.reflix.web.domain.repository;

import com.example.reflix.web.domain.ReviewLookList;
import com.example.reflix.web.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewLookListRepository extends JpaRepository<ReviewLookList,Long> {

    List<ReviewLookList> findAllByUser(User user);


}
