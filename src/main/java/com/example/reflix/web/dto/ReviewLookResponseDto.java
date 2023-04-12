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
public class ReviewLookResponseDto {

    private Long reviewId;
    private Category category;
    private String reviewName;
    private String reviewImageUrl;
    private String reviewVideoUrl;
    private Long contentsId;
}
