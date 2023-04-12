package com.example.reflix.web.controller;


import com.example.reflix.web.dto.UserRegisterDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@RunWith(SpringRunner.class)
//@WebMvcTest(controllers = userController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class userControllerTest {
//
//    @Mock
//    private userRegisterDto user;

    @Autowired
    private MockMvc mvc;

    AutoCloseable openMocks;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(UserController.class).build();
    }

    @DisplayName("로컬 회원 가입 테스트")
    @Test
    public void hireturn() throws Exception {
        String result = "hi";

        mvc.perform((get("/auth/fi")))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    public void join() throws Exception{

        UserRegisterDto user = new UserRegisterDto();
        user.setEmail("email.com");
        user.setName("testName");
        user.setPassword("testpassword");

        String body = objectMapper.writeValueAsString(user);
        mvc.perform(MockMvcRequestBuilders
                        .post("/auth/register")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
//                .andExpect(content().string(user.getEmail()));
    }
}
