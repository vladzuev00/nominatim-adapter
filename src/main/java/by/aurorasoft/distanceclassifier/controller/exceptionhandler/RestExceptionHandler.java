package by.aurorasoft.distanceclassifier.controller.exceptionhandler;

import by.aurorasoft.distanceclassifier.controller.exception.CustomValidationException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

@ControllerAdvice
public final class RestExceptionHandler {
    private static final String MESSAGE_TEMPLATE_ENUM_CONVERSION_FAILED = "%s should be replaced by one of: %s";
    private static final String DELIMITER_ENUM_VALUES = ", ";
    private static final String NOT_VALID_ARGUMENT_MESSAGE_DELIMITER = "; ";
    private static final String FIELD_MESSAGE_DELIMITER = " : ";

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final ConstraintViolationException exception) {
        return createNotAcceptableEntity(exception);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final MethodArgumentNotValidException exception) {
        return createNotAcceptableEntity(getMessage(exception));
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final CustomValidationException exception) {
        return createNotAcceptableEntity(exception);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handle(final ConversionFailedException exception) {
        return createNotAcceptableEntity(getMessage(exception));
    }

    private ResponseEntity<RestErrorResponse> createNotAcceptableEntity(final Exception exception) {
        return createNotAcceptableEntity(exception.getMessage());
    }

    private ResponseEntity<RestErrorResponse> createNotAcceptableEntity(final String message) {
        final HttpStatus status = NOT_ACCEPTABLE;
        final RestErrorResponse response = new RestErrorResponse(status, message, now());
        return new ResponseEntity<>(response, status);
    }

    private String getMessage(final MethodArgumentNotValidException exception) {
        return exception.getFieldErrors()
                .stream()
                .map(this::getMessage)
                .collect(joining(NOT_VALID_ARGUMENT_MESSAGE_DELIMITER));
    }

    private String getMessage(final FieldError error) {
        return error.getField() + FIELD_MESSAGE_DELIMITER + error.getDefaultMessage();
    }

    private String getMessage(final ConversionFailedException exception) {
        return isEnumTarget(exception) ? findEnumConversionFailedMessage(exception) : exception.getMessage();
    }

    private boolean isEnumTarget(final ConversionFailedException exception) {
        return exception.getTargetType()
                .getType()
                .isEnum();
    }

    @SuppressWarnings("unchecked")
    private String findEnumConversionFailedMessage(final ConversionFailedException exception) {
        final Class<? extends Enum<?>> type = (Class<? extends Enum<?>>) exception.getTargetType().getType();
        return format(
                MESSAGE_TEMPLATE_ENUM_CONVERSION_FAILED,
                exception.getValue(),
                findAllowableEnumValues(type)
        );
    }

    private String findAllowableEnumValues(final Class<? extends Enum<?>> type) {
        return stream(type.getEnumConstants())
                .map(Enum::name)
                .collect(joining(DELIMITER_ENUM_VALUES));
    }

    @Value
    static class RestErrorResponse {
        HttpStatus status;
        String message;

        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH-mm-ss")
        LocalDateTime dateTime;
    }
}
