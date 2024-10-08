package com.healthconnect.baseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class InvalidOrExpiredTokenException extends RuntimeException{

    public InvalidOrExpiredTokenException(String message) {
        super(message);
    }

    public InvalidOrExpiredTokenException(String message, Throwable cause) {
        super(message,cause);
    }
}
