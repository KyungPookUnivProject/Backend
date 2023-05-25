package com.example.reflix.web.dto;

import com.example.reflix.web.domain.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentsFavoriteRequestDto {

//    private String year;
//    private String keyword;
//    private String genre;
//    private Category category;


    private List<String> year;
    private List<String> keyword;
    private List<String> genre;
    private List<Category> category;

    public String getGenreAsString() {
        return genre != null ? String.join(" ", genre) : "";
    }
    public String getKeywordAsString() {
        return keyword != null ? String.join(" ", keyword) : "";
    }
    public void setResult(Map<String, List<String>> result) {
        this.year = result.get("year");
        this.genre = result.get("genre");
        this.keyword = result.get("keyword");
        List<String> categoryStrings = result.get("category");
        if (categoryStrings != null) {
            this.category = categoryStrings.stream()
                    .map(Category::valueOf) // 문자열을 Category 열거형으로 변환
                    .collect(Collectors.toList());
        }
    }
    public static ContentsFavoriteRequestDto fromJsonString(String jsonString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ContentsFavoriteRequestDto requestDTO = objectMapper.readValue(jsonString, ContentsFavoriteRequestDto.class);
        return requestDTO;
    }
}
