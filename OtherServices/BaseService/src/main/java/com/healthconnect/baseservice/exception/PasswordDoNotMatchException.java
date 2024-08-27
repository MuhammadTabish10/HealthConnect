package com.healthconnect.baseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PasswordDoNotMatchException extends RuntimeException{

    public PasswordDoNotMatchException(String message) {
        super(message);
    }

    public PasswordDoNotMatchException(String message, Throwable cause) {
        super(message,cause);
    }
}
