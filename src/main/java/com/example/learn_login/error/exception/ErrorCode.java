package com.example.learn_login.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_FOUND(404, "존재하지 않는 페이지 입니다."),
    FORBIDDEN(403, "권한이 없습니다.");

    private final int status;
    private final String message;
}
