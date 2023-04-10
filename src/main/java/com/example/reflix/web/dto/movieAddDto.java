package com.example.reflix.web.dto;

import com.example.reflix.web.domain.category;
import com.example.reflix.web.domain.contentsKeword;
import com.example.reflix.web.domain.recomendContents;
import com.example.reflix.web.domain.review;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class movieAddDto {
    private String contentName;

    private String contentImageUrl;

    private String grade;

    private String runnigTime;

    private String year;

    private category contentsCategory;

    private Long likelist;

    private String story;

}
