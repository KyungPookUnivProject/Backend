package com.example.reflix.web.dto;

import com.example.reflix.web.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsDetailResponseDto {

    private Long contentsId;

    private String contentName;

    private String contentImageUrl;

    private String grade;

    private String runnigTime;

    private String year;

    private Category contentsCategory;

    private Long likelist;

    private String story;

    private List<GenreListResponseDto> genreList;

    private List<KeywordListResponseDto> kewordList;

    private List<Review> reviewList = new ArrayList<>();

    private RecomendContents rcmContents;

}
