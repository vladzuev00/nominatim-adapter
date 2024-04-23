package by.aurorasoft.nominatim.controller.exceptionhandler;

import by.aurorasoft.nominatim.controller.exception.CustomValidationException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Value;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final String MESSAGE_TEMPLATE_NOT_VALID_ENUM_PARAM = "%s should be replaced by one of: %s";
    private static final String DELIMITER_ENUM_PARAM_ALLOWABLE_VALUE = ", ";
    private static final String NOT_VALID_ARGUMENT_MESSAGE_DELIMITER = "; ";

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final ConstraintViolationException exception) {
        return createNotAcceptableResponse(exception);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final MethodArgumentNotValidException exception) {
        return createNotAcceptableResponse(getMessage(exception));
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final CustomValidationException exception) {
        return createNotAcceptableResponse(exception);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(final ConversionFailedException exception) {
        return createNotAcceptableResponse(getMessage(exception));
    }

    private static ResponseEntity<RestErrorResponse> createNotAcceptableResponse(final Exception exception) {
        return createNotAcceptableResponse(exception.getMessage());
    }

    private static ResponseEntity<RestErrorResponse> createNotAcceptableResponse(final String message) {
        final RestErrorResponse errorResponse = new RestErrorResponse(NOT_ACCEPTABLE, message, now());
        return new ResponseEntity<>(errorResponse, NOT_ACCEPTABLE);
    }

    private static String getMessage(final MethodArgumentNotValidException exception) {
        return exception.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining(NOT_VALID_ARGUMENT_MESSAGE_DELIMITER));
    }

    private static String getMessage(final ConversionFailedException exception) {
        return isEnumTarget(exception) ? findEnumParamConversionFailedMessage(exception) : exception.getMessage();
    }

    private static boolean isEnumTarget(final ConversionFailedException exception) {
        return exception.getTargetType().getType().isEnum();
    }

    @SuppressWarnings("unchecked")
    private static String findEnumParamConversionFailedMessage(final ConversionFailedException exception) {
        final Class<? extends Enum<?>> type = (Class<? extends Enum<?>>) exception.getTargetType().getType();
        return format(
                MESSAGE_TEMPLATE_NOT_VALID_ENUM_PARAM,
                exception.getValue(),
                findSeparatedAllowableEnumValues(type)
        );
    }

    private static String findSeparatedAllowableEnumValues(final Class<? extends Enum<?>> type) {
        return stream(type.getEnumConstants())
                .map(Enum::name)
                .collect(joining(DELIMITER_ENUM_PARAM_ALLOWABLE_VALUE));
    }

    @Value
    private static class RestErrorResponse {
        HttpStatus httpStatus;
        String message;

        @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH-mm-ss")
        LocalDateTime dateTime;
    }
}
