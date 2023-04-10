package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.TvRepository;
import com.example.reflix.web.domain.contentLikeListRepository;
import com.example.reflix.web.domain.movie;
import com.example.reflix.web.domain.movieRepository;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TvService implements contentsService{

    private final TvRepository TvRepository;
    private final contentLikeListRepository contentLikeListRepository;
    private final ContentsCommonService contentsService;
    //콘텐츠상세조회
    @Override
    @Transactional
    public contentsDetailResponseDto contentdetail(Long contentId){
        movie contents = TvRepository.findByContentsId(contentId);
        Long likelist = contentLikeListRepository.countByContentId(contentId);
        contentsDetailResponseDto resultdto = contentsDetailResponseDto.builder()
                .contentImageUrl(contents.getImageUrl())
                .contentName(contents.getName())
                .contentsId(contents.getContentsId())
                .contentsCategory(contents.getContentsCategory())
                .year(contents.getYear())
                .grade(contents.getGrade())
                .janre(contents.getJanre().getJanre1())
                .story(contents.getStory())
                .likelist(likelist)
                .build();
        return resultdto;
    }
    //유저에게 취향받아서 분석 후 유사한 콘텐츠 추천
    //유저별 추천된 콘텐츠 목록 저장
    @Transactional
    @Override
    public List<recommendContentsDto> submit(contentFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException {
        String command = "python3";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/Json_sample.py";
        List<recommendContentsDto> contentsList= new ArrayList<>();
        List<String> pyrequestList = new ArrayList<>();
        pyrequestList.add(command);
        pyrequestList.add(arg1);
//        pyrequestList.add(contentFavoriteDto.getContentVariation());
//        pyrequestList.add(contentFavoriteDto.getJangre());
//        pyrequestList.add(contentFavoriteDto.getKeword());
//        pyrequestList.add(contentFavoriteDto.getYear());
        String contentString="{\n";
        contentString+= contentsService.pythonEexc(pyrequestList);
        if(!contentString.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            List<Long> idlist = new ArrayList<>();
            List<similarContentDto> list = Arrays.asList(mapper.readValue(contentString, similarContentDto.class));
            for(similarContentDto dto : list){
                idlist.add(dto.getContentid());
            }
            contentsList = TvRepository.findAllByTvId(idlist);
            for(int i=0;i<contentsList.size();i++){
                contentsList.get(i).setSimir(list.get(i).getSimilarity());
            }
            contentsService.recomendContentsSave(contentsList,userPrincipal.getId());
            return contentsList;
        }
        else{
            return contentsList;
        }
    }
}
