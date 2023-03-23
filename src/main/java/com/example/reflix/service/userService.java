package com.example.reflix.service;

import com.example.reflix.config.auth.CustomUserDetailService;
import com.example.reflix.config.auth.JwtTokenProvider;
import com.example.reflix.config.auth.userPrinciple;
import com.example.reflix.web.domain.Role;
import com.example.reflix.web.domain.user;
import com.example.reflix.web.domain.user1Repository;
import com.example.reflix.web.dto.siginInResponseDto;
import com.example.reflix.web.dto.userDetailResponseDto;
import com.example.reflix.web.dto.userLoginDto;
import com.example.reflix.web.dto.userRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipal;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class userService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final user1Repository user1Repository;
    private final CustomUserDetailService customUserDetailService;

    public siginInResponseDto login(userLoginDto userLoginDto){

        UserDetails userDetails = customUserDetailService.loadUserByUsername(userLoginDto.getEmail());

        String hashPW=bCryptPasswordEncoder.encode(userLoginDto.getPassword());
        log.info("hashPW : " + hashPW + " userdetail"+ userDetails.getPassword());

        if(hashPW.equals(userDetails.getPassword())){
            log.info("비번에러");
            throw new BadCredentialsException(userDetails.getUsername() + "password error");
        }
        log.info("며기부터안됨 authin");
        Authentication authentication =  new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());

        log.info("signIn service | authentication.getName : {}, authentication.getCredentials() : {}",
                authentication.getName(), authentication.getCredentials());

        return new siginInResponseDto(
                "Bearer-"+jwtTokenProvider.createAccessToken(authentication),
                "Bearer-"+jwtTokenProvider.issueRefreshToken(authentication));
    }

    public String register(userRegisterDto userRegisterDto){
        if(user1Repository.findByEmail(userRegisterDto.getEmail()).isPresent()){
            log.info("이메일 중복으로 회원가입 실패"+userRegisterDto.getEmail());
            return "이메일 중복으로 회원가입 실패"+ userRegisterDto.getEmail();
        }
        else{
            String hashPW=bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
            user member = user.builder()
                    .name(userRegisterDto.getName())
                    .password(hashPW)
                    .email(userRegisterDto.getEmail())
                    .Role(Role.USER)
                    .build();
            return user1Repository.save(member).getEmail().toString();
        }
        //유저 회원가입
        //dto에서 이메일,아이디,비번 이름을 받는다
        //디비에서 이메일이 겹치는지 확인
        //이메일이 겹치지않으면 이에일 아이디 저장 비밀번호 인코딩후 저장 이름 저장
        //잘되면 됐다고 리턴 오류나면 왜오류인지 리턴
    }

    public userDetailResponseDto userDetail(userPrinciple user){
        userDetailResponseDto resposnedto = userDetailResponseDto.builder()
                .email(user.getMember().getEmail())
                .name(user.getMember().getName())
                .build();
        return resposnedto;
    }

    public int out(String email,String password,userPrinciple user){
        String hashPW=bCryptPasswordEncoder.encode(password);

        if(email.equals(user.getUsername()) && hashPW.equals(user.getPassword())){
            user1Repository.deleteById(user.getId());
            return 1;
        }
        else{
            return 0;
        }


    }
}
