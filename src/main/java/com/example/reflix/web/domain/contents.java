package com.example.reflix.web.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class contents extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String contentName;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private float grade;

    @Column(nullable = false)
    private String jangre;

    @Column(nullable = false)
    private String runnigTime;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private Integer likelist;

    //keyword의 경우 다중값 처리를 해야되는데
    //컬럼하나에 여러 값이 들어가면 rds의 장점이 없어진다.
    //그러기에 구분자 "|"를 통해 구분한다
    //구분자 "|"를 사용했다는것을 api문서에 남긴다.
    @Column(nullable = false)
    private String keyWord;






}
