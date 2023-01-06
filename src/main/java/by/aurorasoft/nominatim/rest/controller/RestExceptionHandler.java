package by.aurorasoft.nominatim.rest.controller;

import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice
public final class RestExceptionHandler {
    private static final String SEPARATOR_FIELD_NAME_AND_MESSAGE_IN_ERROR = " : ";
    private static final String SEPARATOR_DESCRIPTIONS_FIELDS_ERRORS = ", ";

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(ConstraintException exception) {
        final HttpStatus httpStatus = NOT_ACCEPTABLE;
        final Optional<Errors> optionalErrors = exception.findErrors();
        final String message = optionalErrors
                .map(this::findMessage)
                .orElse(exception.getMessage());
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatus, message, now());
        return new ResponseEntity<>(restErrorResponse, httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(ConstraintViolationException exception) {
        final HttpStatus httpStatus = NOT_ACCEPTABLE;
        final String message = exception.getMessage();
        final RestErrorResponse restErrorResponse = new RestErrorResponse(httpStatus, message, now());
        return new ResponseEntity<>(restErrorResponse, httpStatus);
    }

    private String findMessage(Errors errors) {
        return errors.getFieldErrors().stream()
                .map(RestExceptionHandler::findMessage)
                .collect(joining(SEPARATOR_DESCRIPTIONS_FIELDS_ERRORS));
    }

    private static String findMessage(FieldError fieldError) {
        return fieldError.getField() + SEPARATOR_FIELD_NAME_AND_MESSAGE_IN_ERROR + fieldError.getDefaultMessage();
    }
}
