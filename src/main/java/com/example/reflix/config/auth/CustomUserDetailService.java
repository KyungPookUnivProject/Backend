package com.example.reflix.config.auth;

import com.example.reflix.web.domain.user;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.reflix.web.domain.user1Repository;

import java.util.Optional;


@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailService implements UserDetailsService {

    private final user1Repository user1Repository;
    @Override
    public userAdapter loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<user> user = user1Repository.findByEmail(username);
        log.info(user.get().getPassword() + user.get().getUsername() + user.get().getEmail());
        return new userAdapter(user.get());
//        return user.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

    }
}
