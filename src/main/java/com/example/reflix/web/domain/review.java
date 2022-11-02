package com.example.reflix.web.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class review extends baseTimeEntity{

    //콘텐츠 id당 리뷰리스트로 만들어야한다. 복합키사용 즉 객체에선 리스트 사용
    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long contentId;

    @Column
    private String reviewUrl;

    @Column
    private String reviewImage;

    @Column
    private Long view;//조회수


}
