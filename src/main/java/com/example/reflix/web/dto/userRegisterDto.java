package com.example.reflix.web.dto;

import com.example.reflix.web.domain.Role;
import lombok.Data;

import javax.persistence.Column;

@Data
public class userRegisterDto {
    private String email;
    private String password;
    private String name;
}
