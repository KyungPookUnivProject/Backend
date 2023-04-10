package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.contents;
import com.example.reflix.web.dto.*;

import java.io.IOException;
import java.util.List;

public interface contentsService {
    public contentsDetailResponseDto contentdetail(Long contentId);
//    public String pythonEexc(List<String> list) throws IOException;
//    public void recomendContentsSave(List<recommendContentsDto> contentsList, Long userId);
    public List<recommendContentsDto> submit(contentFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException;



    }
