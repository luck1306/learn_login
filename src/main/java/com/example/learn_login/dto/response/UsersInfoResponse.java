package com.example.learn_login.dto.response;

import com.example.learn_login.entity.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UsersInfoResponse {

    private final Long id;
    private final String accountId;
    private final Role role;

    @Builder
    public UsersInfoResponse(Long id, String accountId, Role role) {
        this.id = id;
        this.accountId = accountId;
        this.role = role;
    }
}
