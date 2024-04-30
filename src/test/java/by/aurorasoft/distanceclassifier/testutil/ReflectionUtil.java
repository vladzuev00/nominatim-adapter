package by.aurorasoft.distanceclassifier.testutil;

import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.springframework.util.ReflectionUtils.findField;

@UtilityClass
public final class ReflectionUtil {

    public static <V, T> V getFieldValue(final T target, final String fieldName, final Class<V> valueType) {
        final Field field = getField(target, fieldName);
        field.setAccessible(true);
        try {
            final Object value = getFieldValue(field, target);
            return valueType.cast(value);
        } finally {
            field.setAccessible(false);
        }
    }

    private static Field getField(final Object target, final String fieldName) {
        return Objects.requireNonNull(findField(target.getClass(), fieldName));
    }

    private static Object getFieldValue(final Field field, final Object target) {
        return ReflectionUtils.getField(field, target);
    }
}
