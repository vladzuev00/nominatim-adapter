package by.aurorasoft.nominatim.controller.exceptionhandler;

import by.aurorasoft.nominatim.base.AbstractJunitSpringBootTest;
import by.aurorasoft.nominatim.controller.exception.CustomValidationException;
import by.aurorasoft.nominatim.controller.exceptionhandler.RestExceptionHandler.RestErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.core.convert.TypeDescriptor.valueOf;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

public final class RestExceptionHandlerTest extends AbstractJunitSpringBootTest {

    @Autowired
    private RestExceptionHandler handler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void constraintViolationExceptionShouldBeHandled() {
        final String givenMessage = "exception-message";
        final ConstraintViolationException givenException = new ConstraintViolationException(givenMessage, emptySet());

        final RestErrorResponse actual = doExpectingNotAcceptable(() -> handler.handle(givenException));
        final RestErrorResponse expected = createNotAcceptableResponse(givenMessage);
        checkMatch(expected, actual);
    }

    @Test
    public void methodArgumentNotValidExceptionShouldBeHandled() {
        final FieldError givenFirstFieldError = createFieldError("first-field", "first-message");
        final FieldError givenSecondFieldError = createFieldError("second-field", "second-message");
        final MethodArgumentNotValidException givenException = createMethodArgumentNotValidException(
                givenFirstFieldError,
                givenSecondFieldError
        );

        final RestErrorResponse actual = doExpectingNotAcceptable(() -> handler.handle(givenException));
        final RestErrorResponse expected = createNotAcceptableResponse(
                "first-field : first-message; second-field : second-message"
        );
        checkMatch(expected, actual);
    }

    @Test
    public void customValidationExceptionShouldBeHandled() {
        final String givenMessage = "exception-message";
        final CustomValidationException givenException = new CustomValidationException(givenMessage);

        final RestErrorResponse actual = doExpectingNotAcceptable(() -> handler.handle(givenException));
        final RestErrorResponse expected = createNotAcceptableResponse(givenMessage);
        checkMatch(expected, actual);
    }

    @Test
    public void conversionFailedExceptionShouldBeHandled() {
        final String givenMessage = "exception-message";
        final ConversionFailedException givenException = createConversionFailedExceptionWithObjectTargetType(givenMessage);

        final RestErrorResponse actual = doExpectingNotAcceptable(() -> handler.handle(givenException));
        final RestErrorResponse expected = createNotAcceptableResponse(givenMessage);
        checkMatch(expected, actual);
    }

    @Test
    public void conversionFailedExceptionWithEnumTargetTypeShouldBeHandled() {
        final ConversionFailedException givenException = createConversionFailedExceptionWithEnumTargetType("exception-value");

        final RestErrorResponse actual = doExpectingNotAcceptable(() -> handler.handle(givenException));
        final RestErrorResponse expected = createNotAcceptableResponse("exception-value should be replaced by one of: FIRST, SECOND, THIRD");
        checkMatch(expected, actual);
    }

    @Test
    public void responseShouldBeMappedToJson()
            throws Exception {
        final RestErrorResponse givenResponse = new RestErrorResponse(
                NOT_ACCEPTABLE,
                "message",
                LocalDateTime.of(2024, 4, 24, 11, 25, 33)
        );

        final String actual = objectMapper.writeValueAsString(givenResponse);
        final String expected = """
                {
                  "status": "NOT_ACCEPTABLE",
                  "message": "message",
                  "dateTime": "2024-04-24 11-25-33"
                }""";
        assertEquals(expected, actual, true);
    }

    private RestErrorResponse doExpectingNotAcceptable(final Supplier<ResponseEntity<RestErrorResponse>> operation) {
        final ResponseEntity<RestErrorResponse> responseEntity = operation.get();
        assertSame(NOT_ACCEPTABLE, responseEntity.getStatusCode());
        return responseEntity.getBody();
    }

    private static RestErrorResponse createNotAcceptableResponse(final String message) {
        return new RestErrorResponse(NOT_ACCEPTABLE, message, null);
    }

    private static void checkMatch(final RestErrorResponse expected, final RestErrorResponse actual) {
        checkEqualsExceptDateTime(expected, actual);
        assertNotNull(actual.getDateTime());
    }

    private static void checkEqualsExceptDateTime(final RestErrorResponse expected, final RestErrorResponse actual) {
        assertSame(expected.getStatus(), actual.getStatus());
        Assert.assertEquals(expected.getMessage(), actual.getMessage());
    }

    private static FieldError createFieldError(final String fieldName, final String message) {
        final FieldError error = mock(FieldError.class);
        when(error.getField()).thenReturn(fieldName);
        when(error.getDefaultMessage()).thenReturn(message);
        return error;
    }

    private static MethodArgumentNotValidException createMethodArgumentNotValidException(final FieldError... errors) {
        final MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getFieldErrors()).thenReturn(asList(errors));
        return exception;
    }

    @SuppressWarnings("SameParameterValue")
    private static ConversionFailedException createConversionFailedExceptionWithObjectTargetType(final String message) {
        final ConversionFailedException exception = mock(ConversionFailedException.class);
        when(exception.getTargetType()).thenReturn(valueOf(TestObject.class));
        when(exception.getMessage()).thenReturn(message);
        return exception;
    }

    @SuppressWarnings("SameParameterValue")
    private static ConversionFailedException createConversionFailedExceptionWithEnumTargetType(final String value) {
        final ConversionFailedException exception = mock(ConversionFailedException.class);
        when(exception.getTargetType()).thenReturn(valueOf(TestEnum.class));
        when(exception.getValue()).thenReturn(value);
        return exception;
    }

    private static final class TestObject {

    }

    private enum TestEnum {
        FIRST, SECOND, THIRD
    }
}
