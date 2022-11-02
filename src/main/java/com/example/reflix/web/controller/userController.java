package com.example.reflix.web.controller;


import com.example.reflix.service.userService;
import com.example.reflix.web.dto.userLoginDto;
import com.example.reflix.web.dto.userRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class userController {

    private final userService userservice;
    //로그인 아이디 비번 보내고 검증하는단계 추후 소셜로그인으로 변경or추가
    @PostMapping("/login")
    public void login(@RequestBody userLoginDto userLoginDto){
        //로그인 후 이유저가 취향 디비가 있는지검사
        //잇으면 content/표시로 redirect
        //없으면 프론트단에서 취향고르는 페이지로 redirect하게함

        //여기서는 db에 취향디비가 있는지만 검사해서 있으면 있다고 리턴
        //없으면 없다고리턴
        //프론트단에서 리턴결과 확인해서 redirect어디로 할지 결정
        userservice.login(userLoginDto);
    }

    //회원가입 회원정보 db에 넣기 추후 소셜로그인 대비해서 유저이메일 받기 이메일을 유니크키로 쓰자. 기본키는 고유번호다.
    @PostMapping("/register")
    public void register(@RequestBody userRegisterDto userRegisterDto){

        //회원가입 이 성공적으로 끝낫는지 아닌지 리턴
        //리턴 결과르 프론트에서 보고 메인페이지로 갈지
        //뭐땜에 회원가입안됐다고 할지 리턴결과로 메시지표시
        userservice.register(userRegisterDto);

    }

    //로그아웃 어떻게 구현해야되는지 모르겟다. 진짜로 시큐리티 사용해야될거같은데
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(){

        return new ResponseEntity("hi", HttpStatus.CREATED);

    }

    //로그인 - > 회원가입 정보입력화면은 정적이기 때문에 프론트단에서 처리한다. rest
}
