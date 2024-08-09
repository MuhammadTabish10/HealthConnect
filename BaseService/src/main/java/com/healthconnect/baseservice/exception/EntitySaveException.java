package com.healthconnect.baseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class EntitySaveException extends RuntimeException {

    public EntitySaveException(String message) {
        super(message);
    }

    public EntitySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
