package com.example.reflix.web.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class userLoginDto {


    private String email;
    private String password;

}
