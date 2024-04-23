package by.aurorasoft.nominatim.validation.validator.coordinate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CoordinateValidatorTest {
    private final TestCoordinateValidator validator = new TestCoordinateValidator();

    @ParameterizedTest
    @MethodSource("provideValueAndExpected")
    public void valueShouldBeValidated(final Double givenValue, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenValue, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> provideValueAndExpected() {
        return Stream.of(
                Arguments.of(-100., true),
                Arguments.of(-100.00000001, false),
                Arguments.of(100., true),
                Arguments.of(100.00000001, false),
                Arguments.of(50.55, true),
                Arguments.of(null, false)
        );
    }

    private static final class TestCoordinateValidator extends CoordinateValidator<Annotation> {
        private static final double MIN_ALLOWABLE = -100;
        private static final double MAX_ALLOWABLE = 100;

        public TestCoordinateValidator() {
            super(MIN_ALLOWABLE, MAX_ALLOWABLE);
        }
    }
}
