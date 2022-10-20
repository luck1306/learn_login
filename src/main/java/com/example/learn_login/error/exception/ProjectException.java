package com.example.learn_login.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter // e.getErrorCode 받아오려면 필수
@RequiredArgsConstructor
public class ProjectException extends RuntimeException{
    private final ErrorCode errorCode;
}
