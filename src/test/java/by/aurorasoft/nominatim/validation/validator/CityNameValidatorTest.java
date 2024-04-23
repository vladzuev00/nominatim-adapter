package by.aurorasoft.nominatim.validation.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class CityNameValidatorTest {
    private final CityNameValidator validator = new CityNameValidator();

    @ParameterizedTest
    @MethodSource("provideNameAndExpected")
    public void nameShouldBeValidated(final String givenName, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenName, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> provideNameAndExpected() {
        return Stream.of(
                Arguments.of("Minsk", true),
                Arguments.of("Gomel", true),
                Arguments.of("Grodno", true),
                Arguments.of("G-ro dno", true),
                Arguments.of("112", false),
                Arguments.of("50.55", false),
                Arguments.of("      ", false),
                Arguments.of("", false),
                Arguments.of(null, false)
        );
    }
}
