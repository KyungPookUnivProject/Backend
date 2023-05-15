package com.example.reflix.web.dto;

import com.example.reflix.web.domain.Category;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentsSearchResponseDto {
    @JsonAlias({"id","contentsId"})
    private Long contentsId;

    @JsonAlias({"name","contentsName"})
    private String contentsName;

    @JsonAlias({"poster_path","contentsImageUrl"})
    private String contentImageUrl;

    @JsonAlias({"media_type","contentsCategory"})
    private Category contentsCategory;
}
