package com.example.reflix.web.controller;

import com.example.reflix.service.contentsService;
import com.example.reflix.service.userService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Log4j2
public class contentsController {

    private final contentsService contentsservice;
    private final userService userservice;
//
//    @ApiOperation(value = "콘텐츠취향을 분석하기",
//            notes = "콘텐츠 종류, 장르, 키워드, 시간대 json으로 body에 담아서 요청," +
//                    "영화 목록을 responsebody에 json으로 응답")
//    @PostMapping("/contents/submit")
//    public ResponseEntity contentSubmit(@RequestBody contentFavoriteRequestDto contentFavoriteDto){
//        log.info("obString");
//        JSONObject ob = contentsservice.submit(contentFavoriteDto);//사용자식별아이디 추가해야됨
//        if(ob !=null || !(ob.toString().equals(""))){
//            String obString=ob.toString();
//            log.info(obString);
//            return new ResponseEntity(ob.toString(), HttpStatus.OK);
//        }
//        else{
//            return new ResponseEntity("no data", HttpStatus.NO_CONTENT);
//        }
//    }
//
//    @ApiOperation(value = "필요한 x,y좌표, title을 해당위치 정보 db에 저장",
//            notes = "필터링된 영화목록들 표시 ")
//    @PostMapping("/contents/recommend")
//    public void contentlook(@RequestBody filterContentsDto filterContentsDto){
//        //그대로 리턴
//        //왜 서브밋에서 바로 추천안하고 여기서 하냐
//        //서브밋해서 필터링 단계와 보여주는 단계를 구분하기위해
//        //dto 간단한 검증후 dto그대로 리턴하면 프론트에서 처리
//    }
//
//    @ApiOperation(value = "선택한 영화에 대한 리뷰영상 조회",
//            notes = "해당영화의 이름을 url에 담아서 요청," +
//                    "해당영화의 리뷰목록을 json으로 응답")
//    @GetMapping("/contents/review")
//    public ResponseEntity reviewlook(@RequestParam String contentName){
//        log.info(contentName);
//        List<reviewResponseDto> allReview = contentsservice.reviewStartSubmit(contentName);
//        return new ResponseEntity(new result(allReview),HttpStatus.OK);
//    }
//    //유저의 리뷰시청목록에 추가
//    @ApiOperation(value = "시청한 리뷰영상 히스토리에 저장",
//            notes = "해당 유저의 email과 리뷰영상id를 바디에 담아서요청받음" +
//                    "리뷰목록에 유저와 조인하여 저장")
//    @PostMapping("/review/look")
//    public void reveiewSave(@RequestBody Long Id,String email){
//        contentsservice.reviewAdd(Id,email);
//    }
//
//    @ApiOperation(value = "콘텐츠 좋아요 기능",
//            notes = "콘텐츠 id와 유저 email을 바디에 담아서 요청받음" +
//                    "콘텐츠테이블에 좋아요횟수 업데이트")
//    @PostMapping("/content/like")
//    public void contentLike(@RequestBody Long Id,String email){
//        contentsservice.contentLike(Id,email);
//    }
//
//    @Scheduled(cron = "0 0 23 * * *", zone = "Asia/Seoul")
//    public ResponseEntity movieUpdate(){
//        //매일 23시마다 크롤링하여 영화정보들이 업데이트된다.
//        //processbuilder를 통해 파이썬파일을 실행시켜 크롤링한다.
//        //마지막은 리뷰클로링으로 끝내자
//        String result = contentsservice.movieCrowling();
//        if(result.equals("sucsess")){
//            reviewUpdate();
//            return new ResponseEntity("sucsess",HttpStatus.OK);
//        }
//        else if(result.contains("error : ")){
//            log.info("contentsController - update error");
//            return new ResponseEntity(result,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        else{
//            return new ResponseEntity(result,HttpStatus.GATEWAY_TIMEOUT);
//        }
//    }
//
//
//    public ResponseEntity reviewUpdate(){
//        String result = contentsservice.reviewCrowling();
//        if(result.equals("sucsess")){
//            return new ResponseEntity("sucsess",HttpStatus.OK);
//        }
//        else if(result.contains("error : ")){
//            return new ResponseEntity(result,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        else{
//            return new ResponseEntity(result,HttpStatus.GATEWAY_TIMEOUT);
//        }
//    }
}
