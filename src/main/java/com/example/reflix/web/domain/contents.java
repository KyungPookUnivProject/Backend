package com.example.reflix.web.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class contents extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsId;

    @Column(nullable = false)
    private String contentName;

    @Column(nullable = false)
    private String contentImageUrl;

    @Column(nullable = true)
    private String grade;

    @Column(nullable = true)
    private String runnigTime;

    @Column(nullable = false)
    private String year;

    @Column(nullable = false)
    private category contentsCategory;

    @Column(nullable = false)
    private int likelist;

    @Column(nullable = false)
    private String story;


    @OneToOne(mappedBy = "contents")
    private contentsJanre janre;

    @OneToMany(mappedBy = "contents")
    private List<contentsKeword> kewordList = new ArrayList<>();

    @OneToMany(mappedBy = "contents")
    private List<review> reviewList = new ArrayList<>();

//    @OneToMany(mappedBy = "contents",fetch = FetchType.LAZY)
//    private recomendContents rcmContents;


//    public int getIntId(){
//        int intid = Math.toIntExact(this.getContentsId());
//        return intid;
//    }

}
