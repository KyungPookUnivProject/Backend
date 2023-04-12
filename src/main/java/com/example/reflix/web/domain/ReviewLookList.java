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
public class ReviewLookList extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String reviewname;

    @Column(nullable = false)
    private String reviewImageUrl;

    @Column(nullable = false)
    private String reviewVideoUrl;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "movieId", insertable = false, updatable = false)
//    private movie movie;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tvserisId", insertable = false, updatable = false)
//    private Tvseris tvseris;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "animationId", insertable = false, updatable = false)
//    private animation animation;
    @Column
    Long contentsId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId", insertable = false, updatable = false)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "reviewId")
    private Long reviewId;
//
//    public void setMember(user1 member) {
//        this.user = member;
//        member.getUserReviewList().add(this);
//    }

}
