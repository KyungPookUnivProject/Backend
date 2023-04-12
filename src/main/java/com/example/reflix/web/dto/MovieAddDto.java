package com.example.reflix.web.dto;

import com.example.reflix.web.domain.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieAddDto {
    private String contentName;

    private String contentImageUrl;

    private String grade;

    private String runnigTime;

    private String year;

    private Category contentsCategory;

    private Long likelist;

    private String story;

}
