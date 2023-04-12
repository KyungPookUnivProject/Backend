package com.example.reflix.web.dto;

import com.example.reflix.web.domain.Category;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsFavoriteRequestDto {

    private String year;
    private String keword;
    private String jangre;
    private Category category;


}
