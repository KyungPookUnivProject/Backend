package com.example.reflix.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Data
@Builder
@AllArgsConstructor
public class reviewResponseDto {

    private Long contentName;

    private String reviewUrl;

    private String reviewImage;

    private Long view;

}
