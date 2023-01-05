package by.aurorasoft.nominatim.rest.validator;

import by.aurorasoft.nominatim.rest.controller.exception.ConstraintException;
import org.junit.Test;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class CityRequestValidatorTest {

    private final CityRequestValidator validator;

    public CityRequestValidatorTest() {
        this.validator = new CityRequestValidator();
    }

    @Test
    public void validationShouldBeSuccessful() {
        final Errors givenErrors = mock(Errors.class);
        when(givenErrors.hasErrors()).thenReturn(false);
        this.validator.validate(givenErrors);
    }

    @Test
    public void validationShouldBeFailed() {
        final Errors givenErrors = mock(Errors.class);
        when(givenErrors.hasErrors()).thenReturn(true);

        ConstraintException expectedException = null;
        try {
            this.validator.validate(givenErrors);
        } catch (final ConstraintException exception) {
            expectedException = exception;
        }

        assertNotNull(expectedException);
        assertSame(expectedException.findErrors().orElseThrow(), givenErrors);
    }
}
