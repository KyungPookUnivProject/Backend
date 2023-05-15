package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.Contents;
import com.example.reflix.web.dto.*;

import java.io.IOException;
import java.util.List;

public interface contentsService {
    public ContentsDetailResponseDto contentdetail(Long contentId);
//    public String pythonEexc(List<String> list) throws IOException;
//    public void recomendContentsSave(List<recommendContentsDto> contentsList, Long userId);
    public List<ContentsRecommendResponseDto> submit(ContentsFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException;
    public List<ContentsDetailDto> search(String q);
    public List<?> getAll(List<Long> list);


    }
