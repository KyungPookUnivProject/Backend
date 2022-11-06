package com.example.reflix.web.controller;

import com.example.reflix.service.contentsService;
import com.example.reflix.web.dto.contentFavoriteRequestDto;
import com.example.reflix.web.dto.filterContentsDto;
import com.example.reflix.web.dto.result;
import com.example.reflix.web.dto.reviewResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Log4j2
public class contentsController {

    private final contentsService contentsservice;
    //제일 첫화면 링크 고민중인게 이거를 로그인 전 화면으로 할지 로그인 후 메인화면을 만들지 미정
    // 로그인 전화면일 경우 프론트단에서 처리
    // 로그인 후 화면일 경우 전에 저장한 회원별 취향에 따라 바로 영화추천 /contents/필터링영화표시로 리다이렉트
    @GetMapping("/")
    public void main(){

    }

    //취향선택 후 취향분석을 위해 서버로 취향을 submit한다. 방식은 get일수도 post일수도 있다.
    @ApiOperation(value = "콘텐츠취향을 분석하기",
            notes = "콘텐츠 종류, 장르, 키워드, 시간대 json으로 body에 담아서 요청")
    @PostMapping("/contents/submit")
    public ResponseEntity contentSubmit(@RequestBody contentFavoriteRequestDto contentFavoriteDto){
        log.info("obString");
        JSONObject ob = contentsservice.submit(contentFavoriteDto);//사용자식별아이디 추가해야됨
        if(ob !=null){
            String obString=ob.toString();
            log.info(obString);
            return new ResponseEntity(ob.toString(), HttpStatus.OK);
        }
        else{
            return new ResponseEntity("no data", HttpStatus.NO_CONTENT);
        }
        //필터린된 콘텐츠목록/콘텐츠마다의 리뷰영상정보를 받으면 콘텐츠목록을 담아서 redirect /contents/필터링영화 표시(추천)
        //redirect는 프론트단에서 하자 항상 백에서하면 의존관계가 꼬인다
        //200일경우 또는 콘텐츠가 없을경우 응답된 responsedto를 담아서 리다이렉트
        //그외 오류일경우 에러페이지 리다이렉트또는 에러메시지 표시
    }
    //content/수집 이후 response status가 200일 경우 redirect되는 url이다.
    //필터린된 영화데이터를 보내준다.
    @ApiOperation(value = "필요한 x,y좌표, title을 해당위치 정보 db에 저장",
            notes = "필터링된 영화목록들 표시 ")
    @PostMapping("/contents/필터링된영화 표시(추천)")
    public void contentlook(@RequestBody filterContentsDto filterContentsDto){
        //그대로 리턴
        //왜 서브밋에서 바로 추천안하고 여기서 하냐
        //서브밋해서 필터링 단계와 보여주는 단계를 구분하기위해
        //dto 간단한 검증후 dto그대로 리턴하면 프론트에서 처리
    }
    //크롤링되어 나온 리뷰영상 누를경우 유튜브리뷰영상이 링크에 내장되게됨 ajax를통해 전체화면 을 바꾸지않고 영상만 나오게하면 좋다
    //또한 누를경우 해당회원이 시청한 영상디비에 저장됨

    @GetMapping("/contents/review/{contentName}")
    public result reviewlook(@PathVariable String contentName){
        log.info(contentName);
        List<reviewResponseDto> allReview = contentsservice.reviewStartSubmit(contentName);
        return new result(allReview);
        //해당 클릭된 리뷰영상의 를 받아온다.
        //넘버를 내가 시청한 리스트에 저장후
        //유튜브영상재생 재생은 프론드 단계에서
        // 리스트저장은 백단계에서
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

    //회원이 다시 취향선택하는 화면은 정적이기때문에 버튼or링크 만들어서 프론트단에서 처리
}
