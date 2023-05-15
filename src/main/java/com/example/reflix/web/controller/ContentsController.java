package com.example.reflix.web.controller;

import com.example.reflix.config.auth.userAdapter;
import com.example.reflix.service.*;
import com.example.reflix.web.domain.*;
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
    private final TvService tvService;
    private final YoutubeServiceImpl youtubeService;
    private final ReviewServiceImpl reviewService;

    private final String KEY ="cb3fbd26fe7fe53cf8af0ba2b6d72370";
    @ApiOperation(value = "콘텐츠취향을 분석하기",
            notes = "콘텐츠 종류, 장르, 키워드, 시간대 json으로 body에 담아서 요청," +
                    "영화 목록을 responsebody에 json으로 응답")
    @PostMapping("/contents/submit")
    public ResponseEntity contentSubmit(@RequestBody ContentsFavoriteRequestDto contentFavoriteDto, @AuthenticationPrincipal userAdapter user) throws IOException, InterruptedException {
//        log.info(user.getAuthorities());
        List<ContentsRecommendResponseDto> contentsList=new ArrayList<>();
        switch (contentFavoriteDto.getCategory().name()){
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
    public ResponseEntity contentdetail(@RequestParam Long contentId,@RequestParam int category){
        ContentsDetailResponseDto contents = null;
        switch (category){
            case 2: contents = animationService.contentdetail(contentId); break;
            case 0 :  contents = movieService.contentdetail(contentId); break;
            case 1 :  contents = tvService.contentdetail(contentId); break;
        }

//        contentsDetailResponseDto contents = contentsservice.contentdetail(contentId);

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
    public ResponseEntity reviewlook(@RequestParam Long contentId,String contentname,int category){
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
    @PostMapping("/content/like")
    public ResponseEntity contentLike(@RequestParam Long contentId,int category,int flag, @AuthenticationPrincipal userAdapter userPrinciple){
        if(flag==1){
            if(contentsservice.contentLike(contentId,category,userPrinciple)){
                log.info("상승");
                return new ResponseEntity(true, HttpStatus.OK);
            }
            else{
                log.info("상승실패 이유무엇");
                return new ResponseEntity(false,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else{
            if(contentsservice.contentDisLike(contentId, userPrinciple.getId())){
                log.info("하락");
                return new ResponseEntity(true, HttpStatus.OK);
            }
            else{
                log.info("하락실패 이유무엇");
                return new ResponseEntity(false,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
    @GetMapping("/review/look/detail")
    public ResponseEntity reviewLookDetail(@AuthenticationPrincipal userAdapter user){
        return new ResponseEntity(reviewService.reviewLookDetail(user.getUser()),HttpStatus.OK);
    }


    @GetMapping("/contents/recommendcontents")
    public ResponseEntity contentsRecommnd(@RequestParam Long ContentsId, int category){

        List<ContentsDetailDto> resultList = new ArrayList<>();
        switch (category){
            case 0 :
                resultList = movieService.getAll(contentsservice.contentRecommend(ContentsId,category)).stream().map(entity -> {
                            ContentsDetailDto dto = new ContentsDetailDto();
                            BeanUtils.copyProperties(entity, dto);
                            return dto;
                        }).collect(Collectors.toList());
                break;
            case 1 :
                resultList = tvService.getAll(contentsservice.contentRecommend(ContentsId,category)).stream().map(entity -> {
                    ContentsDetailDto dto = new ContentsDetailDto();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                }).collect(Collectors.toList());
                break;
            case 2 :
                resultList = animationService.getAll(contentsservice.contentRecommend(ContentsId,category)).stream().map(entity -> {
                    ContentsDetailDto dto = new ContentsDetailDto();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                }).collect(Collectors.toList());
                break;
        }
//        movieService.getAll(contentsservice.contentRecommend(ContentsId,category);
        return new ResponseEntity(resultList,HttpStatus.OK);
    }
    @GetMapping("/contents/search")
    public ResponseEntity contentsSercrh(@RequestParam String q){

        List<ContentsDetailDto> resultList = contentsservice.seacrhContent(q);
//        resultList.addAll(movieService.search(q));
        resultList.addAll(animationService.search(q));
//        resultList.addAll(tvService.search(q));
        return new ResponseEntity(resultList,HttpStatus.OK);
    }
    @PostMapping("/auth/addmovie")
    public void movieadd(@RequestBody MovieAddDto dto){contentsservice.movieadd(dto);
    }



    @GetMapping("/auth/movie/test")
    public List<ContentsDetailDto> getTestMovie(){
        return movieService.getmovieten();
    }


    @GetMapping("/auth/api/save")
    public void getSave(@RequestParam int primary_release_year){

//        movieService.setKey();
//        List<Long> idList = movieService.getIdlist();
        ObjectMapper mapper = new ObjectMapper();
        String result=null;
//        String kewordUrl = null;
        BufferedReader bf = null;
        URL url = null;
        Map<String, Object> jsonMap = null;
        String firstImage = "https://image.tmdb.org/t/p/original";

        try{
            for(int i=1;i<=1000;i++){
                String kewordUrl = "https://api.themoviedb.org/3/discover/movie?api_key="+KEY+"&language=ko&page="+i+"&primary_release_year="+primary_release_year;

//                String apiURL = "https://api.themoviedb.org/3/discover/movie?api_key="+KEY+"&language=ko&page="+i+"&release_date.gte="+startTime+"&release_date.lte="+endTime;
                url = new URL(kewordUrl);

                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                result = bf.readLine();
                jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
                List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");

                List<Long> idList = new LinkedList<>();
                for (Map<String, Object> rid : results) {
                    Long id = Long.parseLong(rid.get("id").toString());
                    idList.add(id);
                    System.out.println(id);
                }

                for(Long id : idList){

                    try {
                        kewordUrl = "https://api.themoviedb.org/3/movie/" + id + "?api_key=" + KEY + "&language=ko-KR&append_to_response=keywords";
                        url = new URL(kewordUrl);
                        bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                        result = bf.readLine();
                        if (result.contains("status_message")) {
                            continue;
                        }

                        jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {
                        });
                        log.info(result);
                    }catch (Exception e) {
                    }

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
                    //영화세부
//                    Movie movie = movieService.getId(id);
                    Object posterPath = jsonMap.get("poster_path");
                    if(posterPath==null){
                        posterPath=null;
                    }
                    else{
                        posterPath=posterPath.toString();
                    }
                    Movie movie = Movie.builder()
                            .contentsId(id)
                            .year(jsonMap.get("release_date").toString())
                            .story(jsonMap.get("overview").toString())
                            .runnigTime(jsonMap.get("runtime").toString())
                            .likelist(0)
                            .Name(jsonMap.get("title").toString())
                            .contentsCategory(Category.MOVIE)
                            .ImageUrl(firstImage+posterPath)
                            .grade(jsonMap.get("adult").toString())
//                            .janre(janre)
//                            .kewordList(keword)
                            .build();

//                    janre.setMovieId(id);
//                    keword.setMovieId(id);
//                    movie.setJanre(janre);
//                    movie.setKewordList(keword);
//                    movie.setJanre(janre);
//                    movie.setKewordList(keword);
//                    movie.setName(jsonMap.get("title").toString());
//                    movie.setStory(jsonMap.get("overview").toString());
//                    movie.setRunnigTime(jsonMap.get("runtime").toString());
//                    movie.setYear(jsonMap.get("release_date").toString());
                    log.info(movie);
                    //3개로 나눠야돼
                    //키워드 , 장르, 영화세부
//                janre.setMovie(movie);
//                keword.setMovie(movie);
//                janre.setMovie(movie);
//                keword.setMovie(movie);
                    movieService.save(movie);

//                    contentsservice.savejanreKeyword(janre,keword);
                }
            }

            }catch (Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());

        }
    }
        //1. movie의 id 전체 긁어옴
        //2. id전체 for문돌면서 id당 영화세부정보,키워드 api로 받아옴
        //3. id movie로 셀렉트해와서 세부정보 한글로 업데이트
        //4. 장르도 한글따로 만들어서 업데이트
        //5. 키워드 일단 영어만해서 업데이트
        //6. 2~5반복


    @GetMapping("/auth/api/getInfo")
    public String getInfo(@RequestParam int id) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "https://api.themoviedb.org/3/movie/" + id + "?api_key=your_api_key";
//        try {
//            Movie movie = restTemplate.getForObject(url, Movie.class);
//            // 영화 세부 정보 처리
//        } catch (HttpClientErrorException.NotFound ex) {
//            // 해당 ID의 영화 정보가 없는 경우 처리
//            // 예를 들어, 로그를 남기거나 다음 ID로 넘어가는 등의 작업 수행
//        }
        try{
            String kewordUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+KEY+"&language=ko-KR&append_to_response=keywords";
            URL url = new URL(kewordUrl);
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();
            log.info(result+123123);

            if(result.contains("status_message")){
                log.info(result);
            }
        }
        catch (Exception e){
            log.info(e.getMessage());
        }
        //yyyy-hh-dd datetype
//
//        String firstImage = "https://image.tmdb.org/t/p/original";
//        int pages = 1;
//        try {
//            for (int i = 1; i <= 1; i++) {
//                String apiURL = "https://api.themoviedb.org/3/movie/11862?api_key=" + KEY
//                        + "&language=ko";
////
//                String apiURL = "https://api.themoviedb.org/3/discover/movie?api_key="+KEY+"&language=ko&page="+i+"&release_date.gte="+starttime+"&release_date.lte="+endtime;
//                URL url = new URL(apiURL);
//
//                BufferedReader bf;
//
//                bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//
//                String result = bf.readLine();
//                log.info(result);
//
//                ObjectMapper mapper = new ObjectMapper();
//                Map<String, Object> jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
//                List<Integer> idList = new ArrayList<>();
//                List<Map<String, Object>> results = (List<Map<String, Object>>) jsonMap.get("results");
//                for (Map<String, Object> rid : results) {
//                    int id = (int) rid.get("id");
//                    idList.add(id);
//                    System.out.println(id);
//                }
//                for(int id : idList){
//                    String kewordUrl = "https://api.themoviedb.org/3/movie/"+id+"?api_key="+KEY+"&language=ko-KR&append_to_response=keywords";
//                    url = new URL(kewordUrl);
//                    bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//
//                    result = bf.readLine();
//                    jsonMap = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
//                    log.info(result);
//                    List<Map<String, Object>> ganresList = (List<Map<String, Object>>) jsonMap.get("genres");
//                    ContentsJanre janre= new ContentsJanre();
//                    int size = ganresList.size();
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
////                    List<contentsKeword> kewordList  = new LinkedList<>();
////                    kewordList.add()
////                    List<Map<String, Object>> bilongd = (List<Map<String, Object>>) jsonMap.get("genres");
//                    Movie movie = Movie.builder()
//                            .contentsId(Long.parseLong(jsonMap.get("id").toString()))
//                            .contentsCategory(Category.MOVIE)
//                            .grade(jsonMap.get("adult").toString())
//                            .ImageUrl(firstImage+jsonMap.get("poster_path").toString())
//                            .janre(janre)
//                            .Name(jsonMap.get("title").toString())
//                            .likelist(0)
//                            .runnigTime(jsonMap.get("runtime").toString())
//                            .story(jsonMap.get("overview").toString())
//                            .year(jsonMap.get("release_date").toString())
////                            .kewordList()
//                            .build();
//                    log.info(movie);
//                    log.info(janre.getJanre1()+janre.getJanre2()+janre.getJanre3()+janre.getJanre4()+janre.getJanre5());
//                }
//
//
//                //지금 스트링으로 영화정보가 나온다 여기서 json으로 바꿔서 영화id만 추출한 후 영화id로 세부정보를 다시 불러온다
//                //세부정보를 json형식으로 dto로 바꿔서 영화세이브
//                //키워드 따로 영화따로 ㄲ 굳굳
//                //   ApiService.getInfo(result);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return "ok";
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
