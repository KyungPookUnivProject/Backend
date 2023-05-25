package com.example.reflix.web.controller;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.service.*;
import com.example.reflix.web.domain.*;
import com.example.reflix.web.domain.repository.ContentsJanreRepository;
import com.example.reflix.web.dto.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
public class ContentsController {

    private final ContentsCommonService contentsservice;
    private final AnimationServiceImpl animationService;
    private final MovieServiceImpl movieService;
    private final TvServiceImpl tvService;
    private final YoutubeServiceImpl youtubeService;
    private final ReviewServiceImpl reviewService;

    private final String KEY ="cb3fbd26fe7fe53cf8af0ba2b6d72370";
    @ApiOperation(value = "콘텐츠취향을 분석하기",
            notes = "콘텐츠 종류, 장르, 키워드, 시간대 json으로 body에 담아서 요청," +
                    "영화 목록을 responsebody에 json으로 응답")
    @PostMapping("/contents/submit")
    public ResponseEntity contentSubmit(@RequestBody String result, @AuthenticationPrincipal userAdapter user) throws IOException, InterruptedException {
        ContentsFavoriteRequestDto contentFavoriteDto =ContentsFavoriteRequestDto.fromJsonString(result);
        log.info(contentFavoriteDto.getGenreAsString());
        log.info(contentFavoriteDto.getKeywordAsString());
        List<ContentsRecommendResponseDto> contentsList=new ArrayList<>();
        switch (contentFavoriteDto.getCategory().get(0).name()){
            case "ANIMATION": contentsList = animationService.submit(contentFavoriteDto,user); break;
            case "MOVIE" :  contentsList = movieService.submit(contentFavoriteDto,user);         log.info("contents id = "+(contentsList.get(0).getContentsId()));
                break;
            case "DRAMA" :  contentsList = tvService.submit(contentFavoriteDto,user); break;
        }
        if(contentsList.size()==0){
            String errMsg = "NO data";
            log.error(errMsg);
            return new ResponseEntity(errMsg, HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity(contentsList, HttpStatus.OK);
        }
    }
    @ApiOperation(value = "필요한 x,y좌표, title을 해당위치 정보 db에 저장",
            notes = "필터링된 영화목록들 표시 ")
    @GetMapping("/contents/detail")
    public ResponseEntity contentdetail(@RequestParam Long contentId,@RequestParam Category category){
        ContentsDetailResponseDto contents = null;
        switch (category.name()){
            case "ANIMATION": contents = animationService.contentdetail(contentId); break;
            case "MOVIE" :  contents = movieService.contentdetail(contentId); break;
            case "DRAMA" :  contents = tvService.contentdetail(contentId); break;
        }

        if(contents==null){
            String errMsg="NO data";
            return new ResponseEntity(errMsg,HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity(contents,HttpStatus.OK);
        }
        //그대로 리턴
        //왜 서브밋에서 바로 추천안하고 여기서 하냐
        //서브밋해서 필터링 단계와 보여주는 단계를 구분하기위해
        //dto 간단한 검증후 dto그대로 리턴하면 프론트에서 처리
    }

    @ApiOperation(value = "선택한 영화에 대한 리뷰영상 조회",
            notes = "해당영화의 이름을 url에 담아서 요청," +
                    "해당영화의 리뷰목록을 json으로 응답")
    @GetMapping("/contents/review")
    public ResponseEntity reviewlook(@RequestParam Long contentId,String contentname,Category category){
        log.info(contentId);
        List<ReviewResponseDto> allReview = reviewService.reviewrecomend(contentId,category);
        if(allReview.isEmpty()){
            allReview = youtubeService.reviewStartSubmit(contentname,contentId,category);
        }
        if(allReview.isEmpty()){
            String errMsg= "No data";
            log.error("contentsController : reviewlook : "+errMsg);
            return new ResponseEntity(errMsg,HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity(allReview,HttpStatus.OK);
        }
    }

    //유저의 리뷰시청목록에 추가
    @ApiOperation(value = "시청한 리뷰영상 히스토리에 저장",
            notes = "해당 유저의 email과 리뷰영상id를 바디에 담아서요청받음" +
                    "리뷰목록에 유저와 조인하여 저장")//시청한 리뷰영상목록에 저장
    @PostMapping("/review/look")
    public ResponseEntity reveiewSave(@RequestBody ReviewLookRequestDto reviewLookRequestDto , @AuthenticationPrincipal userAdapter userPrinciple){
        Long result = reviewService.reviewLookAdd(reviewLookRequestDto,userPrinciple);

        return new ResponseEntity(result,HttpStatus.OK);
    }

    @ApiOperation(value = "콘텐츠 좋아요 기능",
            notes = "콘텐츠 id와 유저 email을 바디에 담아서 요청받음" +
                    "콘텐츠테이블에 좋아요횟수 업데이트")
    @PostMapping("/contents/like")
    public ResponseEntity contentLike(@RequestParam Long contentId,Category category,int flag, @AuthenticationPrincipal userAdapter userPrinciple) {
        if (flag == 1) {
            if (contentsservice.contentLike(contentId, category, userPrinciple)) {
                switch (category.name()) {
                    case "MOVIE":
                        movieService.setContnets(contentId,flag);
                        break;
                    case "DRAMA":
                        tvService.setContnets(contentId,flag);
                        break;
                    case "ANiMATION":
                        animationService.setContnets(contentId,flag);
                        break;
                }
                log.info("상승");
                return new ResponseEntity(true, HttpStatus.OK);
            } else {
                log.info("상승실패 이유무엇");
                return new ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            if (contentsservice.contentDisLike(contentId, userPrinciple.getId())) {

                switch (category.name()) {
                    case "MOVIE":
                        movieService.setContnets(contentId,flag);
                        break;
                    case "DRAMA":
                        tvService.setContnets(contentId,flag);
                        break;
                    case "ANiMATION":
                        animationService.setContnets(contentId,flag);
                        break;
                }
                    log.info("하락");
                    return new ResponseEntity(true, HttpStatus.OK);
                }
            else{
                log.info("하락실패 이유무엇");
                return new ResponseEntity(false, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            }

        }

    @GetMapping("/review/look/detail")
    public ResponseEntity reviewLookDetail(@AuthenticationPrincipal userAdapter user){
        return new ResponseEntity(reviewService.reviewLookDetail(user.getUser()),HttpStatus.OK);
    }


    @GetMapping("/contents/recommendcontents")
    public ResponseEntity contentsRecommnd(@RequestParam Long ContentsId, Category category){

        List<ContentsDetailDto> resultList = new ArrayList<>();

        String genre = null;
        switch (category.name()){
            case "MOVIE" :
                genre = contentsservice.getGenre(ContentsId,category);
                List<Movie> movielist =  movieService.getAll(contentsservice.contentRecommend(genre,category));
                for(Movie rid : movielist){
                    ContentsDetailDto dto = ContentsDetailDto.builder()
                            .year(rid.getYear())
                            .contentsCategory(rid.getContentsCategory())
                            .ImageUrl(rid.getImageUrl())
                            .Name(rid.getName())
                            .contentsId(rid.getContentsId())
                            .build();
                    resultList.add(dto);
                }
                break;
            case "DRAMA" :
                genre = contentsservice.getGenre(ContentsId,category);
                List<Tvseris> tvList = tvService.getAll(contentsservice.contentRecommend(genre,category));
                for(Tvseris rid : tvList){
                    ContentsDetailDto dto = ContentsDetailDto.builder()
                            .year(rid.getYear())
                            .contentsCategory(rid.getContentsCategory())
                            .ImageUrl(rid.getImageUrl())
                            .Name(rid.getName())
                            .contentsId(rid.getContentsId())
                            .build();
                    resultList.add(dto);
                }
                break;
            case "ANIMATION" :
                genre = contentsservice.getGenre(ContentsId,category);
                List<Animation> aniList = animationService.getAll(contentsservice.contentRecommend(genre,category));

                for(Animation rid : aniList) {
                    ContentsDetailDto dto = ContentsDetailDto.builder()
                            .year(rid.getYear())
                            .contentsCategory(rid.getContentsCategory())
                            .ImageUrl(rid.getImageUrl())
                            .Name(rid.getName())
                            .contentsId(rid.getContentsId())
                            .build();
                    resultList.add(dto);
                }
                break;
        }
        return new ResponseEntity(resultList,HttpStatus.OK);
    }

    @GetMapping("/contents/search")
    public ResponseEntity contentsSercrh(@RequestParam String q){

        List<ContentsDetailDto> resultList = contentsservice.seacrhContent(q);
        resultList.addAll(animationService.search(q));
        return new ResponseEntity(resultList,HttpStatus.OK);
    }


    @GetMapping("/contents/idontknowname")
    public ResponseEntity reviewLook(@AuthenticationPrincipal userAdapter user){
        return new ResponseEntity("dkf",HttpStatus.OK);
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
    public ResponseEntity movieUpdate(){
        //매일 23시마다 크롤링하여 영화정보들이 업데이트된다.
        //processbuilder를 통해 파이썬파일을 실행시켜 크롤링한다.
        //마지막은 리뷰클로링으로 끝내자
        String result = contentsservice.movieCrowling();
        if(result.equals("sucsess")){
            reviewUpdate();
            return new ResponseEntity("sucsess",HttpStatus.OK);
        }
        else if(result.contains("error : ")){
            log.info("contentsController - update error");
            return new ResponseEntity(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else{
            return new ResponseEntity(result,HttpStatus.GATEWAY_TIMEOUT);
        }
    }


    public ResponseEntity reviewUpdate(){
        String result = contentsservice.reviewCrowling();
        if(result.equals("sucsess")){
            return new ResponseEntity("sucsess",HttpStatus.OK);
        }
        else if(result.contains("error : ")){
            return new ResponseEntity(result,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else{
            return new ResponseEntity(result,HttpStatus.GATEWAY_TIMEOUT);
        }
    }
}

//
//    @GetMapping("/auth/contents/te")
//    public void getnow(){
//        movieService.now();
//    }
//    @GetMapping("/auth/movie/test")
//    public List<ContentsDetailDto> getTestMovie(){
//        return movieService.getmovieten();
//    }
//
//
//    @GetMapping("/auth/api/save")
//    public void getSave(){
//
//        List<Long> idList = movieService.now();
//        ObjectMapper mapper = new ObjectMapper();
//        String result=null;
//        String kewordUrl = null;
//        BufferedReader bf = null;
//        URL url = null;
//        Map<String, Object> jsonMap = null;
//        String firstImage = "https://image.tmdb.org/t/p/original";
//
//        try{
//                for(Long id : idList){
//
//                    try {
//                        kewordUrl = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + KEY + "&language=ko-KR&append_to_response=keywords";
//                        url = new URL(kewordUrl);
//                        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//                        result = bf.readLine();
//                        if (result.contains("status_message")) {
//                            continue;
//                        }
//
//                        jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {
//                        });
//                        log.info(result);
//                    }catch (Exception e) {
//                    }
//
//                    List<Map<String, Object>> ganresList = (List<Map<String, Object>>) jsonMap.get("genres");
//                    //1. 장르
//                    ContentsJanre janre= new ContentsJanre();
//                    int size = ganresList.size();
//                    if(size==0){
//                        janre.setJanre1("");
//                    }
//                    if(size>=1){
//                        janre.setJanre1(ganresList.get(0).get("name").toString());
//                    }
//                    if(size>=2){
//                        janre.setJanre2(ganresList.get(1).get("name").toString());
//                    }
//                    if(size>=3){
//                        janre.setJanre3(ganresList.get(2).get("name").toString());
//                    }
//                    if(size>=4){
//                        janre.setJanre4(ganresList.get(3).get("name").toString());
//                    }
//                    if(size>=5){
//                        janre.setJanre5(ganresList.get(4).get("name").toString());
//                    }
//
//                    log.info(janre.getJanre1());
//                    Map<String, Object> keywords = (Map<String, Object>) jsonMap.get("keywords");
//                    List<Map<String, Object>> keywordsList = (List<Map<String, Object>>) keywords.get("keywords");
//
//                    contentsKeword keword= new contentsKeword();
//                    int keywordsize = keywordsList.size();
//                    if(keywordsize==0){
//                        keword.setKeyword1("");
//                    }
//                    if(keywordsize>=1){
//                        keword.setKeyword1(keywordsList.get(0).get("name").toString());
//                    }
//                    if(keywordsize>=2){
//                        keword.setKeyword2(keywordsList.get(1).get("name").toString());
//                    }
//                    if(keywordsize>=3){
//                        keword.setKeyword3(keywordsList.get(2).get("name").toString());
//                    }
//                    if(keywordsize>=4){
//                        keword.setKeyword4(keywordsList.get(3).get("name").toString());
//                    }
//                    if(keywordsize>=5){
//                        keword.setKeyword5(keywordsList.get(4).get("name").toString());
//                    }
//                    if(keywordsize>=6){
//                        keword.setKeyword6(keywordsList.get(5).get("name").toString());
//                    }
//                    if(keywordsize>=7){
//                        keword.setKeyword7(keywordsList.get(6).get("name").toString());
//                    }
//
//                    if(keywordsize>=8){
//                        keword.setKeyword8(keywordsList.get(7).get("name").toString());
//                    }
//
//                    if(keywordsize>=9){
//                        keword.setKeyword9(keywordsList.get(8).get("name").toString());
//                    }
//
//                    if(keywordsize>=10){
//                        keword.setKeyword10(keywordsList.get(9).get("name").toString());
//                    }
//
//                    log.info(keword);
//                    Object posterPath = jsonMap.get("poster_path");
//                    if(posterPath==null){
//                        posterPath=null;
//                    }
//                    else{
//                        posterPath=posterPath.toString();
//                    }
//                    Movie movie = Movie.builder()
//                            .contentsId(id)
//                            .year(jsonMap.get("release_date").toString())
//                            .story(jsonMap.get("overview").toString())
//                            .runnigTime(jsonMap.get("runtime").toString())
//                            .likelist(0)
//                            .Name(jsonMap.get("title").toString())
//                            .contentsCategory(Category.MOVIE)
//                            .ImageUrl(firstImage+posterPath)
//                            .grade(jsonMap.get("adult").toString())
//                            .build();
//
//                    janre.setMovieId(id);
//                    keword.setMovieId(id);
//                    movieService.save(movie);
//
//                    contentsservice.savejanreKeyword(janre,keword);
//                }
//        }
//        catch (Exception e){
//
//        }
//
//    }
//
//    @GetMapping("/auth/api/tvsave")
//    public void getTvSave(){
//
//        List<Long> idList = tvService.now();
//        ObjectMapper mapper = new ObjectMapper();
//        String result=null;
//        BufferedReader bf = null;
//        URL url = null;
//        Map<String, Object> jsonMap = null;
//        String firstImage = "https://image.tmdb.org/t/p/original";
//
//        List<ContentsJanre> jList = new LinkedList<>();
//        List<contentsKeword> kList = new LinkedList<>();
//
//        try{
//
//            for(Long id : idList){
//
//                try {
//                    String kewordUrl = "https://api.themoviedb.org/3/tv/" + id + "?api_key=" + KEY + "&language=ko-KR&append_to_response=keywords";
//                    url = new URL(kewordUrl);
//                    bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//                    result = bf.readLine();
//                    if (result.contains("status_message")) {
//                        continue;
//                    }
//
//                    jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {
//                    });
//                    log.info(result);
//                }catch (Exception e) {
//                }
//
//                List<Map<String, Object>> ganresList = (List<Map<String, Object>>) jsonMap.get("genres");
//                //1. 장르
//                ContentsJanre janre= new ContentsJanre();
//                int size = ganresList.size();
//
//                if(size==0){
//                    janre.setJanre1("");
//                }
//                if(size>=1){
//                    janre.setJanre1(ganresList.get(0).get("name").toString());
//                }
//                if(size>=2){
//                    janre.setJanre2(ganresList.get(1).get("name").toString());
//                }
//                if(size>=3){
//                    janre.setJanre3(ganresList.get(2).get("name").toString());
//                }
//                if(size>=4){
//                    janre.setJanre4(ganresList.get(3).get("name").toString());
//                }
//                if(size>=5){
//                    janre.setJanre5(ganresList.get(4).get("name").toString());
//                }
//
//                log.info(janre.getJanre1());
//                Map<String, Object> keywords = (Map<String, Object>) jsonMap.get("keywords");
//                List<Map<String, Object>> keywordsList = (List<Map<String, Object>>) keywords.get("results");
//
//                contentsKeword keword= new contentsKeword();
//                int keywordsize = keywordsList.size();
//                if(keywordsize==0){
//                    keword.setKeyword1(null);
//                }
//                if(keywordsize>=1){
//                    keword.setKeyword1(keywordsList.get(0).get("name").toString());
//                }
//                if(keywordsize>=2){
//                    keword.setKeyword2(keywordsList.get(1).get("name").toString());
//                }
//                if(keywordsize>=3){
//                    keword.setKeyword3(keywordsList.get(2).get("name").toString());
//                }
//                if(keywordsize>=4){
//                    keword.setKeyword4(keywordsList.get(3).get("name").toString());
//                }
//                if(keywordsize>=5){
//                    keword.setKeyword5(keywordsList.get(4).get("name").toString());
//                }
//                if(keywordsize>=6){
//                    keword.setKeyword6(keywordsList.get(5).get("name").toString());
//                }
//                if(keywordsize>=7){
//                    keword.setKeyword7(keywordsList.get(6).get("name").toString());
//                }
//
//                if(keywordsize>=8){
//                    keword.setKeyword8(keywordsList.get(7).get("name").toString());
//                }
//
//                if(keywordsize>=9){
//                    keword.setKeyword9(keywordsList.get(8).get("name").toString());
//                }
//
//                if(keywordsize>=10){
//                    keword.setKeyword10(keywordsList.get(9).get("name").toString());
//                }
//                Object posterPath = jsonMap.get("poster_path");
//                if(posterPath==null){
//                    posterPath=null;
//                }
//                else{
//                    posterPath=posterPath.toString();
//                }
//                Tvseris tv = Tvseris.builder()
//                        .contentsId(id)
//                        .year(jsonMap.get("first_air_date").toString())
//                        .story(jsonMap.get("overview").toString())
//                        .runnigTime(jsonMap.get("episode_run_time").toString())
//                        .likelist(0)
//                        .Name(jsonMap.get("name").toString())
//                        .contentsCategory(Category.DRAMA)
//                        .ImageUrl(firstImage+posterPath)
//                        .grade(jsonMap.get("adult").toString())
//                        .build();
//
//                janre.setTvserisId(id);
//                keword.setTvserisId(id);
//                log.info(tv);
////                tvService.save(tv);
//                jList.add(janre);
//                kList.add(keword);
//
//            }
//            contentsservice.saveJlist(jList);
//            contentsservice.saveKlist(kList);
//
//        }
//        catch (Exception e){
//
//        }
//
//    }
//    @PostMapping("/auth/api/tvsavess")
//    public void getTvSaves(@RequestParam int year){
//        ObjectMapper mapper = new ObjectMapper();
//        String result=null;
//        String kewordUrl = null;
//        BufferedReader bf = null;
//        URL url = null;
//        Map<String, Object> jsonMap = null;
//        String firstImage = "https://image.tmdb.org/t/p/original";
//        try{
//            for(int i=1;i<=25;i++){
//                kewordUrl = "https://api.themoviedb.org/3/discover/tv?api_key="+KEY+"&language=ko&page="+i+"&primary_release_year="+year;
//                url = new URL(kewordUrl);
//
//                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//                result = bf.readLine();
//                jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
//                List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");
//                log.info("페이지는 "+ i);
//                log.info(result);
//                for (Map<String, Object> rid : results) {
//                    Long id = Long.parseLong(rid.get("id").toString());
//                    System.out.println(id);
//                    if(movieService.gets(id).isPresent()){
//                        log.info("중복입니다.");
//                    }
//                    else{
//                        Object posterPath = jsonMap.get("poster_path");
//                        if(posterPath==null){
//                            posterPath=null;
//                        }
//                        else{
//                            posterPath=posterPath.toString();
//                        }
//                        Object overview = jsonMap.get("overview");
//                        if(overview==null){
//                            overview=null;
//                        }
//                        else{
//                            overview=overview.toString();
//                        }
//                        Tvseris movie = Tvseris.builder()
//                                .contentsId(id)
//                                .year(rid.get("first_air_date").toString())
//                                .story(""+overview)
//                                .runnigTime(null)
//                                .likelist(0)
//                                .Name(rid.get("name").toString())
//                                .contentsCategory(Category.DRAMA)
//                                .ImageUrl(firstImage+posterPath)
//                                .grade("false")
//                                .build();
//
//                        tvService.save(movie);
//
//                    }
//
//                }
//            }
//        }
//        catch (Exception e){
//            log.info(e.getMessage());
//        }
//
//    }
//    @PostMapping("/auth/api/savess")
//    public void getSaves(@RequestParam int year){
//        ObjectMapper mapper = new ObjectMapper();
//        String result=null;
//        String kewordUrl = null;
//        BufferedReader bf = null;
//        URL url = null;
//        Map<String, Object> jsonMap = null;
//        String firstImage = "https://image.tmdb.org/t/p/original";
//        try{
//            for(int i=1;i<=100;i++){
//                kewordUrl = "https://api.themoviedb.org/3/discover/movie?api_key="+KEY+"&language=ko&page="+i+"&primary_release_year="+year;
//                url = new URL(kewordUrl);
//
//                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//                result = bf.readLine();
//                jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
//                List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");
//                log.info("페이지는 "+ i);
//                for (Map<String, Object> rid : results) {
//                    Long id = Long.parseLong(rid.get("id").toString());
//                    System.out.println(id);
//                    if(movieService.gets(id).isPresent()){
//                        log.info("중복입니다.");
//                    }
//                    else{
//                        Object posterPath = jsonMap.get("poster_path");
//                        if(posterPath==null){
//                            posterPath=null;
//                        }
//                        else{
//                            posterPath=posterPath.toString();
//                        }
//                        Object overview = jsonMap.get("overview");
//                        if(overview==null){
//                            overview=null;
//                        }
//                        else{
//                            overview=overview.toString();
//                        }
//                        Movie movie = Movie.builder()
//                                .contentsId(id)
//                                .year(rid.get("release_date").toString())
//                                .story(""+overview)
//                                .runnigTime(null)
//                                .likelist(0)
//                                .Name(rid.get("title").toString())
//                                .contentsCategory(Category.MOVIE)
//                                .ImageUrl(firstImage+posterPath)
//                                .grade(rid.get("adult").toString())
//                                .build();
//
//                        movieService.save(movie);
//
//                    }
//
//                }
//            }
//
//        }
//        catch (Exception e){
//            log.info(e.getMessage());
//        }
//
//    }