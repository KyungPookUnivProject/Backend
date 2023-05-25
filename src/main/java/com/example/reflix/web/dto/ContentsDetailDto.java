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
public class ContentsDetailDto {


    private Long contentsId;

    private String Name;

    private String ImageUrl;

    private String year;
    private Category contentsCategory;
}
