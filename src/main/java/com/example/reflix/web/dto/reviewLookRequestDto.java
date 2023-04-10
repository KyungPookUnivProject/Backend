package com.example.reflix.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class reviewLookRequestDto {

    Long contentsId;
    Long reveiwId;
    int category;
}
