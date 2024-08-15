package com.healthconnect.baseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AuthorizationHeaderMissingException extends RuntimeException {

    public AuthorizationHeaderMissingException(String message) {
        super(message);
    }

    public AuthorizationHeaderMissingException(String message, Throwable cause) {
        super(message, cause);
    }
}
