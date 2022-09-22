package com.example.learn_login.dto;

import lombok.Getter;

@Getter
public class TokenDto {

    private String accessToken;

    private String refreshToken;
}
