package com.example.reflix.web.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentFavoriteRequestDto {

    private String year;
    private String keword;
    private String jangre;
    private int category;


}
