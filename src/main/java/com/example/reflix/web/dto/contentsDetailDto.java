package com.example.reflix.web.dto;

import com.example.reflix.web.domain.category;
import com.example.reflix.web.domain.contents;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentsDetailDto {

    private Long contentsId;

    private String contentName;

    private String contentImageUrl;

    private String year;

    private category contentsCategory;

    public contentsDetailDto(contents contents) {
    }
}
