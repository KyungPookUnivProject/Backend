package com.example.reflix.web.controller;


import com.example.reflix.config.auth.JwtTokenProvider;
import com.example.reflix.service.userService;
import com.example.reflix.web.domain.Role;
import com.example.reflix.web.domain.user1;
import com.example.reflix.web.domain.user1Repository;
import com.example.reflix.web.dto.userLoginDto;
import com.example.reflix.web.dto.userRegisterDto;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
@Log4j2
@RestController
@RequiredArgsConstructor
public class userController {

    private final userService userservice;
    private final user1Repository user1Repository;
    private final JwtTokenProvider jwtTokenProvider;
    ///로그인 테스트 코드 ///
    /*
    @PostMapping("/join")
    public String join(){
        user1 user = user1.builder()
                .email("jeaun80@naver.com")
                .name("jingwon")
                .password("hi")
                .Role(Role.USER)
                .likelist("영화").build();
        user1Repository.save(user);
        return user.getEmail();
    }
    */
    ///로그인 테스트코드///
    //로그인 아이디 비번 보내고 검증하는단계 추후 소셜로그인으로 변경or추가
    @PostMapping("/auth/login")
    public ResponseEntity login(@RequestBody userLoginDto userLoginDto){


        log.info(userLoginDto.getEmail());
        String token = userservice.login(userLoginDto);

        if(token==null){
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        else{
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Authorization",token);
            return new ResponseEntity(token,responseHeaders,HttpStatus.OK);
        }

        //로그인 후 이유저가 취향 디비가 있는지검사
        //잇으면 content/표시로 redirect
        //없으면 프론트단에서 취향고르는 페이지로 redirect하게함

        //여기서는 db에 취향디비가 있는지만 검사해서 있으면 있다고 리턴
        //없으면 없다고리턴
        //프론트단에서 리턴결과 확인해서 redirect어디로 할지 결정
    }

    //회원가입 회원정보 db에 넣기 추후 소셜로그인 대비해서 유저이메일 받기 이메일을 유니크키로 쓰자. 기본키는 고유번호다.
    @PostMapping("/auth/register")
    public ResponseEntity register(@RequestBody userRegisterDto userRegisterDto){

        //회원가입 이 성공적으로 끝낫는지 아닌지 리턴
        //리턴 결과르 프론트에서 보고 메인페이지로 갈지
        //뭐땜에 회원가입안됐다고 할지 리턴결과로 메시지표시
        String responseEmail= userservice.register(userRegisterDto);
        if(responseEmail.equals("overlap")){
            String body = "중복된 이메일이 있습니다.";
            return new ResponseEntity(body,HttpStatus.OK);
        }
        else{
            return new ResponseEntity(responseEmail,HttpStatus.OK);
        }
    }

    //로그아웃 어떻게 구현해야되는지 모르겟다. 진짜로 시큐리티 사용해야될거같은데
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(){

        return new ResponseEntity("hi", HttpStatus.CREATED);

    }
    @GetMapping("/test")
    public String test(){
        return "통과해라 제발";
    }

    //로그인 - > 회원가입 정보입력화면은 정적이기 때문에 프론트단에서 처리한다. rest
}
