package com.example.reflix.web.dto;

import com.example.reflix.web.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsDetailGenreDto {

    private Long contentsId;

    private String name;

    private String image_url;

    private String year;

    private Category contentsCategory;

}
