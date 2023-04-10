package com.example.reflix.web.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class review extends baseTimeEntity{

    //콘텐츠 id당 리뷰리스트로 만들어야한다. 복합키사용 즉 객체에선 리스트 사용
    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = true)
    private String contentName;

    @Column(nullable = false)
    private String reviewvideoUrl;

    @Column(nullable = false)
    private String reviewImageurl;

    @Column(nullable = true)
    private BigInteger view;//조회수

    @OneToMany(mappedBy = "review")
    private List<reviewLookList> lookList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tvserisId", insertable = false, updatable = false)
    private Tvseris tvseris;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animationId", insertable = false, updatable = false)
    private animation animation;

    @Column(name = "movieId")
    private Long movieId;

    @Column(name = "tvserisId")
    private Long tvserisId;

    @Column(name = "animationId")
    private Long animationId;
}
