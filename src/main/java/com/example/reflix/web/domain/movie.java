package com.example.reflix.web.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class movie extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentsId;

    @Column(nullable = false)
    private String Name;

    @Column(nullable = false)
    private String ImageUrl;

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

    @Column(nullable = true)
    private String story;


    @OneToOne(mappedBy = "movie")
    private contentsJanre janre;

    @OneToMany(mappedBy = "movie")
    private List<contentsKeword> kewordList = new ArrayList<>();

    @OneToMany(mappedBy = "movie")
    private List<review> reviewList = new ArrayList<>();

}
