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

    @Column
    private Long movieId;
    private Long tvserisId;
    private Long animationId;

    public String getContentsJarne(int index){
        switch (index){
            case 1: return getJanre1();
            case 2: return getJanre2();
            case 3: return getJanre3();
            case 4: return getJanre4();
            case 5: return getJanre5();
            default:
                return null;
        }
    }

    public void setContentsJanre(int index){
        switch (index){
            case 1: setJanre1(getJanre1());
            case 2: setJanre2(getJanre2());
            case 3: setJanre3(getJanre3());
            case 4: setJanre4(getJanre4());
            case 5: setJanre5(getJanre5());
        }
    }
}
