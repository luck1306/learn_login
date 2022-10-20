package com.example.learn_login.error;

import com.example.learn_login.error.exception.ErrorCode;
import com.example.learn_login.error.exception.ProjectException;

public class ForbiddenException extends ProjectException {
    public static final ProjectException EXCEPTION =
            new ForbiddenException();
    private ForbiddenException() {
        super(ErrorCode.FORBIDDEN);
    }
}
