package com.example.reflix.web.dto;

import com.example.reflix.web.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentsDetailResponseDto {

    private Long contentsId;

    private String contentName;

    private String contentImageUrl;

    private String grade;

    private String runnigTime;

    private String year;

    private category contentsCategory;

    private int likelist;

    private contentsJanre janre;

    private List<contentsKeword> kewordList = new ArrayList<>();

    private List<review> reviewList = new ArrayList<>();

    private recomendContents rcmContents;

}
