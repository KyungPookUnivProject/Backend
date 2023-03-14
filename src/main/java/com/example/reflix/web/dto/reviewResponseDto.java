package com.example.reflix.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Data
@Builder
@AllArgsConstructor
public class reviewResponseDto {

    private Long reivewId;

    private Long contentId;

    private String contentName;

    private String reviewvideoUrl;

    private String reviewImageurl;

    private Long view;//조회수

}
