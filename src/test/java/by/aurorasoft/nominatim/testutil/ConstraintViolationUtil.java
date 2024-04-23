package by.aurorasoft.nominatim.testutil;

import lombok.experimental.UtilityClass;

import javax.validation.ConstraintViolation;
import java.util.Set;

@UtilityClass
public final class ConstraintViolationUtil {

    public static <T> String findFirstMessage(final Set<ConstraintViolation<T>> violations) {
        return violations.iterator()
                .next()
                .getMessage();
    }
}
