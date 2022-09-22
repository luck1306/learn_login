package com.example.learn_login.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class RequestForSignUp {

    @NotEmpty
    private String accountId;

    @NotEmpty
    private String password;
}
