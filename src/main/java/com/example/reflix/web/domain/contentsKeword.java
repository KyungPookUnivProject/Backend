package com.example.reflix.web.domain;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentsKeword extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kewordId;

    @OneToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)
    private Movie movie;
    @OneToOne
    @JoinColumn(name = "tvserisId", insertable = false, updatable = false)
    private Tvseris tvseris;
    @OneToOne
    @JoinColumn(name = "animationId", insertable = false, updatable = false)
    private Animation animation;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "movieId", insertable = false, updatable = false)
//    private Movie movie;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tvserisId", insertable = false, updatable = false)
//    private Tvseris tvseris;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "animationId", insertable = false, updatable = false)
//    private Animation animation;

    @Column
    private String keyword1;
    @Column
    private String keyword2;
    @Column
    private String keyword3;
    @Column
    private String keyword4;
    @Column
    private String keyword5;
    @Column
    private String keyword6;
    @Column
    private String keyword7;
    @Column
    private String keyword8;
    @Column
    private String keyword9;
    @Column
    private String keyword10;
    @Column
    private String koreaKeyword1;
    @Column
    private String koreaKeyword2;
    @Column
    private String koreaKeyword3;
    @Column
    private String koreaKeyword4;
    @Column
    private String koreaKeyword5;
    @Column
    private String koreaKeyword6;
    @Column
    private String koreaKeyword7;
    @Column
    private String koreaKeyword8;
    @Column
    private String koreaKeyword9;
    @Column
    private String koreaKeyword10;

    @Column
    private Long movieId;
    private Long tvserisId;
    private Long animationId;

    //한컬럼에 여러키워드 박는다. 구분자를 사용 ex) 사랑,판타지,군대,소총,바다,전쟁
    //인공지능파트랑 협의해봐야됨

}
