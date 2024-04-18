package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class NumberUtil {

    public static int assertPositive(final int value, final String message) {
        if (value <= 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
