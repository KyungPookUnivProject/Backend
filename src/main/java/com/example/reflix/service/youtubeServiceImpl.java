package com.example.reflix.service;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.reflix.web.domain.reviewRepository;
import com.example.reflix.web.dto.reviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class youtubeServiceImpl implements youtubeService {

    private final reviewRepository reviewRepository;


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
    @Transactional
    public List<reviewResponseDto> reviewStartSubmit(Long contentId){
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
        return reviewRepository.findById(contentId).stream()
                .map(m -> new reviewResponseDto(m.getId(),m.getContentId(),m.getContentName(),m.getReviewvideoUrl(),m.getReviewImageurl(),m.getView()))
                .collect(Collectors.toList());
    }
//
//    /** Global instance properties filename. */
//    private static String PROPERTIES_FILENAME = "youtube.properties";
//
//    /** Global instance of the HTTP transport. */
//    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//
//    /** Global instance of the JSON factory. */
//    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
//
//    /**
//     * Global instance of the max number of videos we want returned (50 = upper
//     * limit per page).
//     */
//    private static final long NUMBER_OF_VIDEOS_RETURNED = 1;
//
//    /** Global instance of Youtube object to make all API requests. */
//    private static YouTube youtube;
//
//    /**
//     * Initializes YouTube object to search for videos on YouTube
//     * (Youtube.Search.List). The program then prints the names and thumbnails of
//     * each of the videos (only first 50 videos).
//     *
//     * @param args command line args.
//     * @throws IOException
//     */
//
//    // youtube url 가져오기
//    @Override
//    public ArrayList<Music> selectUrlByTitle() {
//        String musicurl = "";
//        ArrayList<Music> result = new ArrayList<Music>();
//
//        try {
//
//            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
//                public void initialize(HttpRequest request)
//                        throws IOException {
//                }})
//                    .setApplicationName("youtube-cmdline-search-sample")
//                    .build();
//            SearchListResponse searchResponse = null;
//            YouTube.Search.List search = youtube.search().list("id,snippet");
//            search.setKey("유튜브 API KEY");
//            search.setType("video");
//            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
//            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
//            search.setQ("키워드 입력");
//            searchResponse = search.execute();
//            System.out.println(temp.get(j).getmUrl());
//            List<SearchResult> searchResultList = searchResponse.getItems();
//
//            if (searchResultList != null) {
//                SearchResult singleVideo = searchResultList.get(0);
//                ResourceId rId = singleVideo.getId();
//                musicurl = rId.getVideoId();
//                temp.get(j).setmUrl("https://www.youtube.com/watch?v=" + musicurl);
//                result.add(temp.get(j));
//                System.out.println("\n-------------------------------------------------------------\n");
////					musicurl = prettyPrint(searchResultList.iterator(), queryTerm);
//
//            }
//
//        } catch (GoogleJsonResponseException e) {
//            j--;
//            System.err.println(
//                    "There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
//            continue;
//        } catch (IOException e) {
//            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
//            break;
//        } catch (Throwable t) {
//            // Use a logger to log this exception
//            // t.printStackTrace();
//            break;
//        }
//
//        return result;
//    }
}
