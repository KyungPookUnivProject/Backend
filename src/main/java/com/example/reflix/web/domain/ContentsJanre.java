package com.example.reflix.web.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsJanre extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contentsJanreId;

    @Column(nullable = false)
    private String janre1;
    @Column
    private String janre2;
    @Column
    private String janre3;
    @Column
    private String janre4;
    @Column
    private String janre5;

    //사실 장르에서 콘텐츠를 불러올 필요가 없기때문에 양방향일 필요가없다. 필요없다 사실
    @OneToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;
    @OneToOne
    @JoinColumn(name = "tvserisId", insertable = false, updatable = false)
    private Tvseris tvseris;
    @OneToOne
    @JoinColumn(name = "animationId", insertable = false, updatable = false)
    private Animation animation;
}
