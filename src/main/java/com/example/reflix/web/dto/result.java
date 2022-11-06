package com.example.reflix.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class result<T> {

    private T data;
}
