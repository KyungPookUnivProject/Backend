package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.*;
import com.example.reflix.web.domain.repository.AnimationRepository;
import com.example.reflix.web.domain.repository.ContentsLikeListRepository;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnimationServiceImpl implements contentsService{

    private final AnimationRepository aniRepository;
    private final ContentsLikeListRepository contentsLikeListRepository;
    private final ContentsCommonService contentsService;
    //콘텐츠상세조회
    @Override
    @Transactional
    public ContentsDetailResponseDto contentdetail(Long contentId){
        Animation contents = aniRepository.findByContentsId(contentId);
        Long likelist = contentsLikeListRepository.countByContentId(contentId);
        ContentsDetailResponseDto resultdto = ContentsDetailResponseDto.builder()
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
    public List<ContentsRecommendResponseDto> submit(ContentsFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException {
        String command = "python3";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/completion/animation_keyword_recommend.py";
        List<ContentsRecommendResponseDto> contentsList= new LinkedList<>();
        List<String> pyrequestList = new ArrayList<>();

        pyrequestList.add(command);
        pyrequestList.add(arg1);
        pyrequestList.add(contentFavoriteDto.getJangre());
        pyrequestList.add(contentFavoriteDto.getKeword());
        String contentString= contentsService.pythonEexc(pyrequestList);
        if(contentString!=null){
            ObjectMapper mapper = new ObjectMapper();
            List<Long> idlist = new ArrayList<>();
            List<SimilarContentsDto> list = Arrays.asList(mapper.readValue(contentString, SimilarContentsDto[].class));
            for(SimilarContentsDto dto : list){
                idlist.add(dto.getTmdbId());
            }int IntstartDate = Integer.parseInt(contentFavoriteDto.getYear().toString().substring(0,4));
            int IntendDate = IntstartDate+10;
            String startDate = String.valueOf(IntstartDate)+"-01-01";
            String endDate = String.valueOf(IntendDate)+"-01-01";
            contentsList = aniRepository.findAllByAniId(idlist,startDate,endDate);

            if(contentsList.size()==0){
                return contentsList;
            }
            for(int i=0;i<contentsList.size();i++){
                contentsList.get(i).setSimir(90);
            }
            contentsService.recomendContentsSave(contentsList,userPrincipal.getId());
            return contentsList;
        }
        else{
            return contentsList;
        }
    }
//
//    @Transactional
//    @Override
//    public List<ContentsRecommendResponseDto> submit(ContentsFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException {
//        String command = "python3";
//        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/Json_sample.py";
//        List<ContentsRecommendResponseDto> contentsList= new ArrayList<>();
//        List<String> pyrequestList = new ArrayList<>();
//        pyrequestList.add(command);
//        pyrequestList.add(arg1);
////        pyrequestList.add(contentFavoriteDto.getContentVariation());
//        pyrequestList.add(contentFavoriteDto.getJangre());
////        pyrequestList.add(contentFavoriteDto.getKeword());
////        pyrequestList.add(contentFavoriteDto.getYear());
//        String contentString = contentsService.pythonEexc(pyrequestList);
//        if(contentString!=null){
//            ObjectMapper mapper = new ObjectMapper();
//            List<Long> idlist = new ArrayList<>();
//            List<SimilarContentsDto> list = Arrays.asList(mapper.readValue(contentString, SimilarContentsDto[].class));
//            for(SimilarContentsDto dto : list){
//                idlist.add(dto.getTmdbId());
//            }
//            contentsList = aniRepository.findAllByAniId(idlist);
//            for(int i=0;i<contentsList.size();i++){
//                contentsList.get(i).setSimir(90);
////                contentsList.get(i).setSimir(list.get(i).getSimilarity());
//            }
//            contentsService.recomendContentsSave(contentsList,userPrincipal.getId());
//            return contentsList;
//        }
//        else{
//            return contentsList;
//        }
//    }
//
    @Override
    @Transactional
    public List<ContentsDetailDto> search(String q){
        //필요한건 q를 나눈다 띄어쓰기별로
        //나눈거를 db에서 자연어 검색을 찾는다.
        //이때 넣어야되는게 키워드,이름
        q = "%"+q+"%";
        List<Animation> list = aniRepository.findByNameSearch(q);
        List<ContentsDetailDto> resultList = new ArrayList<>();
        for(Animation rid : list){
            ContentsDetailDto dto  = ContentsDetailDto.builder()
                    .contentsId(rid.getContentsId())
                    .contentName(rid.getName())
                    .contentImageUrl(rid.getImageUrl())
                    .contentsCategory(rid.getContentsCategory())
                    .year(rid.getYear()).build();
            resultList.add(dto);
        }
        return resultList;
    }

    public List<Animation> getAll(List<Long> idList){
        return aniRepository.findAllById(idList);
    }
}
