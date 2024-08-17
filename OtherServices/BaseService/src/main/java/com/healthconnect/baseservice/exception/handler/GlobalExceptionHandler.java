package com.healthconnect.baseservice.exception.handler;

import com.healthconnect.baseservice.constant.ErrorMessages;
import com.healthconnect.baseservice.exception.*;
import com.healthconnect.baseservice.dto.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.healthconnect.baseservice.util.ExceptionUtils.buildResponseEntity;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionMessage<String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntitySaveException.class)
    public ResponseEntity<ExceptionMessage<String>> handleEntitySaveException(EntitySaveException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityUpdateException.class)
    public ResponseEntity<ExceptionMessage<String>> handleEntityUpdateException(EntityUpdateException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityDeleteException.class)
    public ResponseEntity<ExceptionMessage<String>> handleEntityDeleteException(EntityDeleteException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtTokenMissingException.class)
    public ResponseEntity<ExceptionMessage<String>> handleJwtTokenMissingException(JwtTokenMissingException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<ExceptionMessage<String>> handleJwtTokenInvalidException(JwtTokenInvalidException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionMessage<String>> handleUserNotFoundException(UserNotFoundException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationHeaderMissingException.class)
    public ResponseEntity<ExceptionMessage<String>> handleAuthorizationHeaderMissingException(AuthorizationHeaderMissingException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ExceptionMessage<String>> handleUnAuthorizedException(UnAuthorizedException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage<String>> handleGenericException(Exception ex) {
        return buildResponseEntity(ErrorMessages.UNEXPECTED_ERROR + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
