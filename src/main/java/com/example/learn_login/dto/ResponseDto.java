package com.example.learn_login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseDto {

    private String a;

    @Builder
    public ResponseDto(String a) {
        this.a = a;
    }
}
