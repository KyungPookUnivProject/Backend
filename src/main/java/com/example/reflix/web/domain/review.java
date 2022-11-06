package com.example.reflix.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class review extends baseTimeEntity{

    //콘텐츠 id당 리뷰리스트로 만들어야한다. 복합키사용 즉 객체에선 리스트 사용
    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long contentName;

    @Column
    private String reviewUrl;

    @Column
    private String reviewImage;

    @Column
    private Long view;//조회수


}
