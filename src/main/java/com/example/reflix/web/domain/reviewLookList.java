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
public class reviewLookList extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String contentsname;

    @Column(nullable = false)
    private String reviewname;

    @Column(nullable = false)
    private String contentsImageUrl;

    @Column(nullable = false)
    private String reviewImageUrl;

    @Column(nullable = false)
    private String reviewVideoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentsId", insertable = false, updatable = false)
    private contents contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private user user;
//
//    public void setMember(user1 member) {
//        this.user = member;
//        member.getUserReviewList().add(this);
//    }

}
