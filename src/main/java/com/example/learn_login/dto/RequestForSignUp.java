package com.example.learn_login.dto;

import com.example.learn_login.entity.User;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestForSignUp {

    @NotNull
    private String accountId;

    @NotNull
    private String password;

    public User toUser() {
        return User.builder()
                .accountId(this.accountId)
                .password(this.password)
                .build();
    }
}
