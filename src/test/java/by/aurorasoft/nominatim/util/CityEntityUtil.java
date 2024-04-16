package by.aurorasoft.nominatim.util;

import by.aurorasoft.nominatim.crud.model.entity.CityEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class CityEntityUtil {

    public static void checkEquals(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
    }
}
