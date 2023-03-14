package com.example.reflix.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.reflix.web.domain.user1Repository;


@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {


    private final user1Repository user1Repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        userPrinciple user = null;
//        user = user1Repository.findByEmail(username);
//        return user;

        return user1Repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
//    private UserDetails createUserDetails(Member member) {
//        // 권한 관리 테이블로 만든 깃
//        // -> https://github.com/szerhusenBC/jwt-spring-security-demo/blob/master/src/main/java/org/zerhusen/security/model/User.java
//        List<SimpleGrantedAuthority> grantedAuthorities = member.getRoleList().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority))
//                .collect(Collectors.toList());
//
//        return new User(member.getUserId(),
//                member.getPw(),
//                grantedAuthorities);
//    }

}
