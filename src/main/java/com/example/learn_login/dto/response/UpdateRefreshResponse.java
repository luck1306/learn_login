package com.example.learn_login.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRefreshResponse {

    private String beforeRefreshToken;

    private String afterRefreshToken;

    @Builder
    public UpdateRefreshResponse(String before, String after) {
        this.beforeRefreshToken = before;
        this.afterRefreshToken = after;
    }
}
