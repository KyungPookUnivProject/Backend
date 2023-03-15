package com.example.reflix.web.controller;


import com.example.reflix.config.auth.JwtTokenProvider;
import com.example.reflix.service.userService;
import com.example.reflix.web.domain.Role;
import com.example.reflix.web.domain.user;
import com.example.reflix.web.domain.user1Repository;
import com.example.reflix.web.dto.siginInResponseDto;
import com.example.reflix.web.dto.userDetailResponseDto;
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

@Log4j2
@RestController
@RequiredArgsConstructor
public class userController {

    private final userService userservice;
    private final user1Repository user1Repository;
    private final JwtTokenProvider jwtTokenProvider;
    ///로그인 테스트 코드 ///

    @GetMapping("/auth/fi")
    public String test1(){
        return "hi";
    }
    @PostMapping("/join")
    public String join(){
        user user = com.example.reflix.web.domain.user.builder()
                .email("jeaun80@naver.com")
                .name("jingwon")
                .password("hi")
                .Role(Role.USER)
                .build();
        user1Repository.save(user);
        return user.getEmail();
    }

    ///로그인 테스트코드///
    //로그인 아이디 비번 보내고 검증하는단계 추후 소셜로그인으로 변경or추가
    @PostMapping("/auth/login")
    public siginInResponseDto login(@RequestBody userLoginDto userLoginDto){

        log.info(userLoginDto.getEmail());
        siginInResponseDto token = userservice.login(userLoginDto);
        log.info(token);
        return token;
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
            return new ResponseEntity(body,HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity(responseEmail,HttpStatus.OK);
        }
    }

    @GetMapping("/user/detail/{email}")
    public ResponseEntity userDetail(String email){

        userDetailResponseDto dto = userservice.userDetail(email);
        return new ResponseEntity(dto,HttpStatus.OK);
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
