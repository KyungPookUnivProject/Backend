package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.*;
import com.example.reflix.web.domain.repository.ContentsLikeListRepository;
import com.example.reflix.web.domain.repository.TvRepository;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TvServiceImpl implements contentsService{

    private final TvRepository TvRepository;
    private final ContentsLikeListRepository contentsLikeListRepository;
    private final ContentsCommonService contentsService;
    //콘텐츠상세조회
    @Override
    @Transactional
    public ContentsDetailResponseDto contentdetail(Long contentId){
        Tvseris contents = TvRepository.findByContentsId(contentId);
//        Long likelist = contentsLikeListRepository.countByContentId(contentId);
        Long likelist = Long.valueOf(contents.getLikelist());
        ContentsJanre janre = contents.getJanre();
        contentsKeword keword = contents.getKewordList();
        List<GenreListResponseDto> JanreList = new LinkedList<>();
        List<KeywordListResponseDto> KeywordList = new LinkedList<>();

        for (int i = 1; i <= 5; i++) {
            String jarne = janre.getContentsJarne(i);
            if (jarne != null) {
                JanreList.add(new GenreListResponseDto(jarne));
            }
            else{
                break;
            }
        }

        for (int i = 1; i <= 10; i++) {
            String keyword = keword.getKeyword(i);
            if (keyword != null) {
                KeywordList.add(new KeywordListResponseDto(keyword));
            }
            else{
                break;
            }
        }
        ContentsDetailResponseDto resultdto = ContentsDetailResponseDto.builder()
                .contentImageUrl(contents.getImageUrl())
                .contentName(contents.getName())
                .contentsId(contents.getContentsId())
                .contentsCategory(contents.getContentsCategory())
                .year(contents.getYear())
                .grade(contents.getGrade())
                .genreList(JanreList)
                .kewordList(KeywordList)
                .story(contents.getStory())
                .likelist(likelist)
                .runnigTime(contents.getRunnigTime())
                .build();
        return resultdto;
    }
    //유저에게 취향받아서 분석 후 유사한 콘텐츠 추천
    //유저별 추천된 콘텐츠 목록 저장
    @Transactional
    @Override
    public List<ContentsRecommendResponseDto> submit(ContentsFavoriteRequestDto contentFavoriteDto, userAdapter userPrincipal) throws IOException {
        String command = "python3";
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/completion/tv_keywords_recommend.py";
        List<ContentsRecommendResponseDto> contentsList= new LinkedList<>();
        List<String> pyrequestList = new ArrayList<>();
        pyrequestList.add(command);
        pyrequestList.add(arg1);
        pyrequestList.add(contentFavoriteDto.getGenreAsString());
        pyrequestList.add(contentFavoriteDto.getKeywordAsString());
        String contentString= contentsService.pythonEexc(pyrequestList);
        if(contentString!=null){
            ObjectMapper mapper = new ObjectMapper();
            List<Long> idlist = new ArrayList<>();
            List<SimilarContentsDto> list = Arrays.asList(mapper.readValue(contentString, SimilarContentsDto[].class));
            for(SimilarContentsDto dto : list){
                idlist.add(dto.getTmdbId());
            }

            int IntstartDate = Integer.parseInt(contentFavoriteDto.getYear().toString().substring(1,5));
            int IntendDate = IntstartDate+10;
            String startDate = String.valueOf(IntstartDate)+"-01-01";
            String endDate = String.valueOf(IntendDate)+"-01-01";
            contentsList = TvRepository.findAllByTvId(idlist,startDate,endDate);
            log.info("size+ ; "+contentsList.size());
            if(contentsList.size()==0){
                return contentsList;
            }
            for(int i=0;i<contentsList.size();i++){
                contentsList.get(i).setSimir(90);
            }
//            contentsService.recomendContentsSave(contentsList,userPrincipal.getId());
            return contentsList;
        }
        else{
            return contentsList;
        }
    }
    @Override
    @Transactional
    public List<ContentsDetailDto> search(String q){
        //필요한건 q를 나눈다 띄어쓰기별로
        //나눈거를 db에서 자연어 검색을 찾는다.
        //이때 넣어야되는게 키워드,이름
        q = "%"+q+"%";
        List<Tvseris> list = TvRepository.findByNameSearch(q);
        List<ContentsDetailDto> resultList = new ArrayList<>();
        for(Tvseris rid : list){
            ContentsDetailDto dto  = ContentsDetailDto.builder()
                    .contentsId(rid.getContentsId())
                    .Name(rid.getName())
                    .ImageUrl(rid.getImageUrl())
                    .contentsCategory(rid.getContentsCategory())
                    .year(rid.getYear()).build();
            resultList.add(dto);
        }
        return resultList;
    }


    @Override
    public void setContnets(Long ContentsId,int flag) {
        Tvseris movie = TvRepository.findById(ContentsId).get();
        if(flag == 1){
            movie.setLikelist(movie.getLikelist()+1);

        }else{
            movie.setLikelist(movie.getLikelist()-1);

        }
        TvRepository.save(movie);
    }

    public List<Tvseris> getAll(List<Long> idList){
        return TvRepository.findAllById(idList);
    }


    public List<Long> now(){
        LocalDateTime dateTime;
        return TvRepository.findNowId("2023-05-23");
    }

    @Transactional
    public void save(Tvseris tv){
        TvRepository.save(tv);
    }
    public List<Long> getIdlist(){
        return TvRepository.findAllId();
    }
}
