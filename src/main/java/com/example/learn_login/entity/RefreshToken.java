package com.example.learn_login.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RedisHash
public class RefreshToken {

    @Id
    private String accountId;

    private String value;

    @TimeToLive
    private Long exp;

    @Builder
    public RefreshToken (String accountId, String value) {
        this.accountId = accountId;
        this.value = value;
        this.exp = 43200L;
    }
}
