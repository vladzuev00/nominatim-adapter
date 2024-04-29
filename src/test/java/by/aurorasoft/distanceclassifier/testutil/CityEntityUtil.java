package by.aurorasoft.distanceclassifier.testutil;

import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity;
import by.aurorasoft.distanceclassifier.crud.model.entity.CityEntity.CityGeometry;
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
        assertSame(expected.getType(), actual.getType());
        checkEquals(expected.getGeometry(), actual.getGeometry());
    }

    public static void checkEqualsExceptId(final List<CityEntity> expected, final List<CityEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEqualsExceptId(expected.get(i), actual.get(i)));
    }

    public static void checkEquals(final CityGeometry expected, final CityGeometry actual) {
        assertTrue(expected.getGeometry().equalsTopo(actual.getGeometry()));
        assertTrue(expected.getBoundingBox().equalsTopo(actual.getBoundingBox()));
    }
}
