package com.example.reflix.web.dto;

import com.example.reflix.web.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class filterContentsDto {

    private Long contentsId;

    private String contentName;

    private String contentImageUrl;

    private int simir;
    @OneToMany(mappedBy = "contents")
    private List<contentsKeword> kewordList = new ArrayList<>();

    public filterContentsDto(Long contentsId, int simir) {
    }

    public filterContentsDto(contents contents) {
    }
}
