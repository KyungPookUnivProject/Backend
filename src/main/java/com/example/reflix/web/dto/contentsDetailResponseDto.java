package com.example.reflix.web.dto;

import com.example.reflix.web.domain.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class contentsDetailResponseDto {

    private Long contentsId;

    private String contentName;

    private String contentImageUrl;

    private String grade;

    private String runnigTime;

    private String year;

    private category contentsCategory;

    private int likelist;

    @OneToOne(mappedBy = "contents")
    private contentsJanre janre;

    private List<contentsKeword> kewordList = new ArrayList<>();

    @OneToMany(mappedBy = "contents")
    private List<review> reviewList = new ArrayList<>();

    @OneToOne(mappedBy = "contents",fetch = FetchType.LAZY)
    private recomendContents rcmContents;

}
