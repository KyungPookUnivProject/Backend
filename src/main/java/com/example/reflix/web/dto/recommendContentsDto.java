package com.example.reflix.web.dto;

import com.example.reflix.web.domain.category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class recommendContentsDto {

    private Long contentsId;

    private String Name;

    private String ImageUrl;

    private category contentsCategory;
    private int Simir = 0;

}
