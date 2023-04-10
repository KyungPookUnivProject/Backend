package com.example.reflix.web.domain;

import jdk.jfr.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class contentLikeList extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

//    @JoinColumn(name = "movieId")
//    private Long movieid;
//    @JoinColumn(name = "tvserisId")
//    private Long tvseris;
//    @JoinColumn(name = "animationId")
//    private Long animation;
//    @OneToOne
    @JoinColumn(name = "contentsId")
    private Long contents;
    @Column
    private category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private user user;


    @Column(name = "contentsId")
    private Long contentId;
}
