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
        return user1Repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }
}
