package com.example.reflix.web.domain;

import lombok.Setter;
import org.json.simple.JSONObject;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.*;

@Entity
@Setter
//db연결되는 도메인 이름은 추후 변경 예정
public class user1 extends baseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private String like;

}
