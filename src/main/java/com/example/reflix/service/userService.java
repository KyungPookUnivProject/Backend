package com.example.reflix.service;

import com.example.reflix.config.auth.JwtTokenProvider;
import com.example.reflix.web.domain.Role;
import com.example.reflix.web.domain.user;
import com.example.reflix.web.domain.user1Repository;
import com.example.reflix.web.dto.userDetailResponseDto;
import com.example.reflix.web.dto.userLoginDto;
import com.example.reflix.web.dto.userRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class userService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final user1Repository user1Repository;
    public String login(userLoginDto userLoginDto){

//        user member = user1Repository.findByEmail(userLoginDto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("노가입"));
//
//
//        String hashPW=bCryptPasswordEncoder.encode(userLoginDto.getPassword());
//
//        if(hashPW.equals(member.getPassword())){
//            return jwtTokenProvider.createToken(member.getEmail(),member.getRole());
//        }
//        else{
//            return null;
//        }
        //dto에서 id pwd 구분해서 만든다음 디비에있는 id와같은지 검사후 같으면
        //비번검사 비번이 인코딩해서 디비에서 인코딩된 비번과 같으면 true리턴
        return null;
    }

    public String register(userRegisterDto userRegisterDto){
        String hashPW=bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
        user member = user.builder()
                .name(userRegisterDto.getName())
                .password(hashPW)
                .email(userRegisterDto.getEmail())
                .Role(Role.USER)
                .build();
        if(user1Repository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            return "overlap";
        }
        else{
            return user1Repository.save(member).getEmail().toString();
        }

        //유저 회원가입
        //dto에서 이메일,아이디,비번 이름을 받는다
        //디비에서 이메일이 겹치는지 확인
        //이메일이 겹치지않으면 이에일 아이디 저장 비밀번호 인코딩후 저장 이름 저장
        //잘되면 됐다고 리턴 오류나면 왜오류인지 리턴
    }

    public userDetailResponseDto userDetail(String email){
        Optional<user> Optionaluser = user1Repository.findByEmail(email);
        user user = Optionaluser.orElseThrow(UnsupportedOperationException::new);
        userDetailResponseDto resposnedto = userDetailResponseDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
        return resposnedto;
    }

    public void mypage(String email){

    }
}
