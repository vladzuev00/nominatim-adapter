package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.rest.controller.exception.CustomValidationException;
import by.aurorasoft.nominatim.rest.controller.exception.NoSuchEntityException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public final class RestExceptionHandler {
    private static final String SEPARATOR_FIELD_NAME_AND_MESSAGE_IN_ERROR = " : ";
    private static final String MESSAGE_UNKNOWN_ERROR = "unknown error";

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(ConstraintViolationException exception) {
        return handleValidationException(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(MethodArgumentNotValidException exception) {
        return handleValidationException(findMessage(exception.getFieldError()));
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(CustomValidationException exception) {
        return handleValidationException(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(NoSuchEntityException exception) {
        return handleNotFoundException(exception.getMessage());
    }

    private static ResponseEntity<RestErrorResponse> handleNotFoundException(String message) {
        return handleException(NOT_FOUND, message);
    }

    private static ResponseEntity<RestErrorResponse> handleValidationException(String message) {
        return handleException(NOT_ACCEPTABLE, message);
    }

    private static ResponseEntity<RestErrorResponse> handleException(HttpStatus httpStatus, String message) {
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatus, message, now());
        return new ResponseEntity<>(restErrorResponse, httpStatus);
    }

    private static String findMessage(FieldError fieldError) {
        return fieldError != null
                ? fieldError.getField() + SEPARATOR_FIELD_NAME_AND_MESSAGE_IN_ERROR + fieldError.getDefaultMessage()
                : MESSAGE_UNKNOWN_ERROR;
    }
}
