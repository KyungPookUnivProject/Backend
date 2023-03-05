package com.example.reflix.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private Long Id;

    @Column(nullable = false)
    private String contentName;

    @Column(nullable = false)
    private String reviewvideoUrl;

    @Column(nullable = false)
    private String reviewImageurl;

    @Column(nullable = false)
    private Long view;//조회수

    @OneToMany(mappedBy = "review")
    private List<reviewLookList> lookList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contentsId", insertable = false, updatable = false)
    private contents contents;

}
