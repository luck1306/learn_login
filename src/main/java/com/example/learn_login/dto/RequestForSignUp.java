package com.example.learn_login.dto;

import com.example.learn_login.entity.User;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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

    public UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(this.accountId, this.password);
    }
}
