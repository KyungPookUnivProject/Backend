package com.example.reflix.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class siginInResponseDto {

    private String accessToken;
    private String refreshToken;
}
