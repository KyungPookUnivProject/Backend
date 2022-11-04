package com.example.reflix.service;

import com.example.reflix.web.dto.userLoginDto;
import com.example.reflix.web.dto.userRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class userService {

    public void login(userLoginDto userLoginDto){
        //dto에서 id pwd 구분해서 만든다음 디비에있는 id와같은지 검사후 같으면
        //비번검사 비번이 인코딩해서 디비에서 인코딩된 비번과 같으면 true리턴
    }

    public void register(userRegisterDto userRegisterDto){
        //유저 회원가입
        //dto에서 이메일,아이디,비번 이름을 받는다
        //디비에서 이메일이 겹치는지 확인
        //이메일이 겹치지않으면 이에일 아이디 저장 비밀번호 인코딩후 저장 이름 저장
        //잘되면 됐다고 리턴 오류나면 왜오류인지 리턴
    }
}
