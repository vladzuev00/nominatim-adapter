package by.aurorasoft.mileagecalculator.testutil;

import by.aurorasoft.mileagecalculator.crud.model.entity.CityEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.*;

@UtilityClass
public final class CityEntityUtil {

    public static void checkEquals(final CityEntity expected, final CityEntity actual) {
        checkEqualsExceptId(expected, actual);
        assertEquals(expected.getId(), actual.getId());
    }

    public static void checkEqualsExceptId(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getName(), actual.getName());
        assertTrue(expected.getGeometry().equalsTopo(actual.getGeometry()));
        assertSame(expected.getType(), actual.getType());
        assertTrue(expected.getBoundingBox().equalsTopo(actual.getBoundingBox()));
    }

    public static void checkEqualsExceptId(final List<CityEntity> expected, final List<CityEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEqualsExceptId(expected.get(i), actual.get(i)));
    }
}
