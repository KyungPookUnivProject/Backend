package com.example.reflix.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class contentsSaveDto {

    public String adult;
    public String original_title;
    public String overview;
    public String poster_path;
    public String release_date;
    public String title;

}
