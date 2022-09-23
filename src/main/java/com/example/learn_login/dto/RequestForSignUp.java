package com.example.learn_login.dto;

import com.example.learn_login.entity.User;
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

    public User toUser() {
        return User.builder()
                .accountId(this.accountId)
                .password(this.password)
                .build();
    }
}
