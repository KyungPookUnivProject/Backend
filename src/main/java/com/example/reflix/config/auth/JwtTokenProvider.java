package com.example.reflix.config.auth;

import com.example.reflix.web.domain.Role;
import com.example.reflix.web.domain.refreshToken;
import com.example.reflix.web.domain.refreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Base64;
import java.util.Date;
import java.security.Key;

import java.util.List;

@Component
@Log4j2
@Configuration
//@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {

    private final CustomUserDetailService myUserDetailsService;
    private final refreshTokenRepository refreshTokenRepository;

    private final String secretKey;
    private final long tokenValidityInMs;
    private final long refreshTokenValidityInMs;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey,
                            @Value("${jwt.token-validity-in-sec}") long tokenValidity,
                            @Value("${jwt.refresh-token-validity-in-sec}") long refreshTokenValidity,
                            CustomUserDetailService myUserDetailsService,
                            refreshTokenRepository refreshTokenRepository){
        this.secretKey = secretKey;
        this.tokenValidityInMs = tokenValidity * 1000;
        this.refreshTokenValidityInMs = refreshTokenValidity * 1000;
        this.myUserDetailsService = myUserDetailsService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    private Key key;

    @Override
    public void afterPropertiesSet() throws Exception {  // init()
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now) // 발행시간
                .signWith(key, SignatureAlgorithm.HS512) // 암호화
                .setExpiration(validity) // 만료
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        userAdapter userDetails = myUserDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public JwtCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return JwtCode.ACCESS;
        } catch (ExpiredJwtException e){
            // 만료된 경우에는 refresh token을 확인하기 위해
            return JwtCode.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            log.info("jwtException : {}", e);
        }
        return JwtCode.DENIED;
    }

    @Transactional
    public String reissueRefreshToken(String refreshToken) throws RuntimeException{
        // refresh token을 디비의 그것과 비교해보기
        Authentication authentication = getAuthentication(refreshToken);
        refreshToken findRefreshToken = refreshTokenRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("userId : " + authentication.getName() + " was not found"));

        if(findRefreshToken.getToken().equals(refreshToken)){
            // 새로운거 생성
            String newRefreshToken = createRefreshToken(authentication);
            findRefreshToken.changeToken(newRefreshToken);
            return newRefreshToken;
        }
        else {
            log.info(findRefreshToken.getToken());
            log.info(refreshToken);
            log.info("refresh 토큰이 일치하지 않습니다. ");
            return null;
        }
    }

    @Transactional
    public String issueRefreshToken(Authentication authentication){
        String newRefreshToken = createRefreshToken(authentication);

        // 기존것이 있다면 바꿔주고, 없다면 만들어줌
        refreshTokenRepository.findByUserId(authentication.getName())
                .ifPresentOrElse(
                        r-> {r.changeToken(newRefreshToken);
                            log.info("issueRefreshToken method | change token ");
                        },
                        () -> {
                            refreshToken token = refreshToken.createToken(authentication.getName(), newRefreshToken);
                            log.info(" issueRefreshToken method | save tokenID : {}, token : {}", token.getUserId(), token.getToken());
                            refreshTokenRepository.save(token);
                        });

        return newRefreshToken;
    }

    private String createRefreshToken(Authentication authentication){
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMs);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public static enum JwtCode{
        DENIED,
        ACCESS,
        EXPIRED;
    }
//
//    private String secretKey = "myprojectsecret";
//    private long tokenValidTime = 30 * 60 * 1000L;
//    private final CustomUserDetailService userDetailsService;
//
//    @PostConstruct
//    public void init(){
//        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
//    }
//
//    public String createToken(String userPk, Role role){
//        Claims claims = Jwts.claims().setSubject(userPk);
//        claims.put("roles", role.getKey()); // 정보는 key / value 쌍으로 저장된다.
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + tokenValidTime)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
//                .compact();
//    }
//    public boolean valiadToken(String token){
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//    public String resolveToken(HttpServletRequest request){
//        return request.getHeader("Authorization");
//    }
//
//    public String getUserPk(String token) {
//        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//    }
}
