package com.example.reflix.web.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class refreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long id; // userId

    private String userId;
    private String token;


    private refreshToken(String id, String token) {
        userId = id;
        this.token = token;
    }

    public static refreshToken createToken(String userId, String token){
        return new refreshToken(userId, token);
    }

    public void changeToken(String token) {
        this.token = token;
    }
}
