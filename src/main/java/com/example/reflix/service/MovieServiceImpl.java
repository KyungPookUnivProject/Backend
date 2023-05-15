package com.example.reflix.service;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.web.domain.*;
import com.example.reflix.web.domain.repository.ContentsLikeListRepository;
import com.example.reflix.web.domain.repository.MovieRepository;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieServiceImpl implements contentsService{

    private final MovieRepository movieRepository;
    private final ContentsLikeListRepository contentsLikeListRepository;
    private final ContentsCommonService contentsService;
    private final String KEY ="cb3fbd26fe7fe53cf8af0ba2b6d72370";

    //콘텐츠상세조회
    @Override
    @Transactional
    public ContentsDetailResponseDto contentdetail(Long contentId){
        Optional<Movie> OptionalContents = movieRepository.findByContentsId(contentId);
        Movie contents= new Movie();
        OptionalContents.orElse(contents);

        Long likelist = contentsLikeListRepository.countByContentId(contentId);
        if(OptionalContents.isPresent()==false){
            contents = saves(contentId);
        }else{
            contents = OptionalContents.get();
        }

        ContentsDetailResponseDto resultdto = ContentsDetailResponseDto.builder()
                .contentImageUrl(contents.getImageUrl())
                .contentName(contents.getName())
                .contentsId(contents.getContentsId())
                .contentsCategory(contents.getContentsCategory())
                .year(contents.getYear())
                .grade(contents.getGrade())
                .janre("test data없음")
//                .janre(contents.getJanre().getJanre1())
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
        String arg1 = "/Users/gimjingwon/PycharmProjects/pythonProject1/completion/movie_keyword_recommend.py";
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
            }
            int IntstartDate = Integer.parseInt(contentFavoriteDto.getYear().toString().substring(0,4));
            int IntendDate = IntstartDate+10;
            String startDate = String.valueOf(IntstartDate)+"-01-01";
            String endDate = String.valueOf(IntendDate)+"-01-01";
            contentsList = movieRepository.findAllBymovieId(idlist,startDate,endDate);
            log.info("size+ ; "+contentsList.size());
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

    @Override
    public List<ContentsDetailDto> search(String q){
        //필요한건 q를 나눈다 띄어쓰기별로
        //나눈거를 db에서 자연어 검색을 찾는다.
        //이때 넣어야되는게 키워드,이름
        q = "%"+q+"%";
        List<Movie> list = movieRepository.findByNameSearch(q);
        List<ContentsDetailDto> resultList = new ArrayList<>();
        for(Movie rid : list){
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

    public List<Movie> getAll(List<Long> idList){
        return movieRepository.findAllById(idList);
    }

    public List<ContentsDetailDto> getmovieten(){
        List<Movie> list = movieRepository.findAll(PageRequest.of(0, 10)).getContent();
        List<ContentsDetailDto> resultList = new ArrayList<>();

        for(Movie rid : list){
            ContentsDetailDto dto  = ContentsDetailDto.builder()
                    .contentsId(rid.getContentsId())
                    .contentName(rid.getName())
                    .contentImageUrl(rid.getImageUrl())
                    .contentsCategory(rid.getContentsCategory())
                    .year(rid.getYear()).build();
            resultList.add(dto);
        }
        return  resultList;
    }
    public List<Long> getIdlist(){
        return movieRepository.findAllId();
    }

    @Transactional
    public void save(Movie movie){
         movieRepository.save(movie);

    }


    @Transactional
    public Movie saves(Long contentsId){
        String firstImage = "https://image.tmdb.org/t/p/original";
        Movie movie = null;

        try {
            String kewordUrl = "https://api.themoviedb.org/3/movie/" + contentsId + "?api_key=" + KEY + "&language=ko-KR&append_to_response=keywords";
            URL url = new URL(kewordUrl);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();
            ObjectMapper mapper = new ObjectMapper();
            Map<String,Object> jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });

            Object posterPath = jsonMap.get("poster_path");
            if(posterPath==null){
                posterPath=null;
            }
            else{
                posterPath=posterPath.toString();
            }
            movie = Movie.builder()
                    .contentsId(contentsId)
                    .year(jsonMap.get("release_date").toString())
                    .story(jsonMap.get("overview").toString())
                    .runnigTime(jsonMap.get("runtime").toString())
                    .likelist(0)
                    .Name(jsonMap.get("title").toString())
                    .contentsCategory(Category.MOVIE)
                    .ImageUrl(firstImage+posterPath)
                    .grade(jsonMap.get("adult").toString())
                    .build();
            log.info(result);
        }catch (Exception e) {
        }
        movieRepository.save(movie);
        return movie;

    }
//    public void setKey(){
//        movieRepository.setKey();
//    };

    public Movie getId(Long Id){
        return movieRepository.findByContentsId(Id).get();
    }
}
