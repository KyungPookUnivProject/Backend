package com.example.reflix.config.auth;

import com.example.reflix.web.domain.User;
import lombok.Getter;

@Getter
public class userAdapter extends userPrinciple{

    private User user;

    public userAdapter(User user){
        super(user);
        this.user = user;
    }


}
