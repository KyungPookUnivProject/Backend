package com.example.reflix.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class reviewLookList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private user1 user;

    @Column
    private String imageUrl;

    @Column
    private String videoUrl;
//
//    public void setMember(user1 member) {
//        this.user = member;
//        member.getUserReviewList().add(this);
//    }

}
