package com.example.reflix.config.auth;

import com.example.reflix.web.domain.user;
import lombok.Getter;

@Getter
public class userAdapter extends userPrinciple{

    private user user;

    public userAdapter(user user){
        super(user);
        this.user = user;
    }


}
