package by.aurorasoft.nominatim.validation.validator.coordinate;

import org.junit.Test;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CoordinateValidatorTest {
    private final TestCoordinateValidator validator = new TestCoordinateValidator();

    @Test
    public void valueShouldBeValid() {
        final double givenValue = 100;
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenValue, givenContext);
        assertTrue(actual);

        verifyNoInteractions(givenContext);
    }

    @Test
    public void valueShouldNotBeValid() {
        final double givenValue = -100.00000001;
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenValue, givenContext);
        assertFalse(actual);

        verifyNoInteractions(givenContext);
    }

    private static final class TestCoordinateValidator extends CoordinateValidator<Annotation> {
        private static final double MIN_ALLOWABLE = -100;
        private static final double MAX_ALLOWABLE = 100;

        public TestCoordinateValidator() {
            super(MIN_ALLOWABLE, MAX_ALLOWABLE);
        }
    }
}
