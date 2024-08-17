package com.healthconnect.baseservice.util;

import com.healthconnect.baseservice.dto.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ExceptionUtils {
    public static <T> ResponseEntity<ExceptionMessage<T>> buildResponseEntity(T error, HttpStatus status) {
        ExceptionMessage<T> exceptionMessage = ExceptionMessage.<T>builder()
                .error(error)
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(exceptionMessage, status);
    }
}
