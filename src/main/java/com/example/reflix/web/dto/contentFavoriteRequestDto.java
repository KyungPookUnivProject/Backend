package com.example.reflix.web.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentFavoriteRequestDto {


//    private Long Id;//사용자 식별 id즉
    private String year;
    private String keword;
    private String jangre;
    private String contentVariation;


}
