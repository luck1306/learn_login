package com.example.learn_login.error;

import com.example.learn_login.error.exception.ErrorCode;
import com.example.learn_login.error.exception.ProjectException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends ProjectException {
    public static final ProjectException EXCEPTION =
            new NotFoundException();
    private NotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}
