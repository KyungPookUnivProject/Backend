package com.example.reflix.service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.example.reflix.web.domain.Category;
import com.example.reflix.web.dto.ReviewResponseDto;
import com.google.api.services.youtube.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import com.google.api.services.youtube.YouTube;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class YoutubeServiceImpl implements YoutubeService {

    private final ReviewServiceImpl reviewservice;

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final GsonFactory JSON_FACTORY     = new GsonFactory();
    private static final String PROPERTIES_FILENAME     = "GOOGLEYOUTUBE";
    private static final String GOOGLE_YOUTUBE_URL      = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_SEARCH_TYPE     = "video";
    private static final String YOUTUBE_SEARCH_FIELDS   = "items(id/videoId,snippet)";
    private static final String YOUTUBE_VIDEO_FIELDS    = "itmes(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url,statistics/viewCount)";
    private static final String YOUTUBE_API_APPLICATION = "google-youtube-api-search";
    private static final String YOUTUBE_APIKEY_ENV = "AIzaSyDlvrqAWcYBQ3SjFyTwzRmY6MnGyLYFoqE";
    private static final long NUMBER_OF_VIDEOS_RETURNED  = 5;
    private static YouTube youtube;

    static {
        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {
                        // intentionally left empty
                    }
                }).setApplicationName(YOUTUBE_API_APPLICATION).build();
    }


    public List<ReviewResponseDto> getVideoList(List<SearchResult> searchResultList, String query, Long contentId){
        List<ReviewResponseDto> reviewResponseDtoList= new ArrayList<>();
        List<Video> videosResultList = new ArrayList<>();

        if(searchResultList.isEmpty()){
            log.info("getvideolist() 전에 서치못함 id를");
        }
        else{
            log.info("hi"+searchResultList.get(0).getId());
        }
        try{
            YouTube.Videos.List youlist = youtube.videos().list("statistics,player");
            youlist.setKey(YOUTUBE_APIKEY_ENV);
            youlist.setFields(YOUTUBE_VIDEO_FIELDS);
            for(SearchResult dto : searchResultList){
                youlist.setId(dto.getId().getVideoId());
                VideoListResponse videoListResponse = youlist.execute();
                videosResultList = videoListResponse.getItems();
                if(videosResultList.isEmpty()){
                    log.info("비디오찾기 익스큐트결과 없음");
                }
                else{
                    log.info("faj"+videosResultList.get(0).getId());
                }
                for(Video rid : videosResultList){
                    log.info(rid.getId());
                    log.info(rid.getSnippet().getTitle());
                    reviewResponseDtoList.add(
                            ReviewResponseDto.builder()
                            .reviewImageurl(rid.getSnippet().getTitle())
                            .videoId(rid.getId())
                            .view(rid.getStatistics().getViewCount())
                            .build());
                }
            }
            return reviewResponseDtoList;
        }catch (Exception e){
            e.getMessage();
        }
        return reviewResponseDtoList;
    }


    @Transactional
    public List<ReviewResponseDto> reviewStartSubmit(String query, Long contentId, Category category){

        log.info("start youtube"+ query);
        List<ReviewResponseDto> resultlist = new ArrayList<>();

        try{
            if(youtube !=null){
                YouTube.Search.List search = youtube.search().list("id,snippet");
                String apiKey = YOUTUBE_APIKEY_ENV;
                search.setKey(apiKey);
                String prequrey = null;
                switch (category.name()){
                    case "MOVIE" : prequrey = "영화 "; break;
                    case "DRAMA" : prequrey = "드라마"; break;
                    case "ANIMATION" : prequrey = "애니메이션"; break;
                }
                search.setQ(prequrey+query+" 리뷰");
                search.setOrder("relevance");
                search.setType(YOUTUBE_SEARCH_TYPE);
                String youTubeFields = YOUTUBE_SEARCH_FIELDS;
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
                if (youTubeFields != null && !youTubeFields.isEmpty()) {
                    search.setFields(youTubeFields);
                } else {
                    search.setFields(YOUTUBE_SEARCH_FIELDS);
                }
                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();
                if (searchResultList != null) {
                    for (SearchResult rid : searchResultList) {
                        log.info("나만봐 "+rid.getSnippet().getDescription() + "쿼리 = "+ prequrey);
                        if(rid.getSnippet().getDescription().contains(prequrey)){
                            ReviewResponseDto item = ReviewResponseDto.builder()
                                    .contentId(contentId)
                                    .reviewName(query)
                                    .videoId(rid.getId().getVideoId())
                                    .reviewName(rid.getSnippet().getTitle())
                                    .reviewImageurl(rid.getSnippet().getThumbnails().getHigh().getUrl())
                                    .videoId(GOOGLE_YOUTUBE_URL+rid.getId().getVideoId())
                                    .build();
                            resultlist.add(item);
                            log.info("title : "+ item.getReviewName());
                        }

                    }
                    reviewservice.reviewSave(resultlist,category);
                    return resultlist;
                }
            }

        } catch (GoogleJsonResponseException e){
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch(IOException e){
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch(Throwable t){
            t.printStackTrace();
        }
        log.info("여기서?");
        return resultlist;
    }
        //contentsname을 받아서 name+ reivew라고 유튜브api를 이용해 받고 리턴한다 그대로

        //youtubeapi사용해서
        //리뷰영상은 콘텐츠네임 + 리뷰 를 유튜브api에 넣어서 관련 영상목록 json으로 받아온다.
        //유튜브api사용을 줄이기 위해 콘텐츠네임+리뷰를 더한다.
        // 로직 자
        // 한번 조회한 영상목록json은 저장한다. 리뷰목록에
        // if(콘텐츠의 출시년도가 2년이내면 항상 조회한다.) why? 실시간으로 리뷰영상이 올라온다
        // 엤날콘텐츠의 경우 새로운 리뷰영상은 잘올라오지않는다.
        // 왜이렇게 하냐 why
        // youtube api가 속도가 생각보다 느리다.
        // 사용자가 많아지면 둘다 느려진다.
        // 그느데 api는 막을방법이없고
        // db조회방식은 테이블을 나누거나 인덱스로 속도개선 여지가 있다.

    public void reviewLookedAdd(Long reviewId, Principal principal){

//        Optional<reviewLookList> reviewLookList = reviewLookListRepository.findById(Id);
//        reviewLookListRepository.save();
//        String email = principal.getName();
//        reviewLookList reviewLookList = new reviewLookList()
//        reviewLookListRepository.save()
//        Optional<user> user = user1Repository.findByEmail(email);
//
////        reviewLookList.builder().
//        reviewLookListRepository.save()
    }
}
