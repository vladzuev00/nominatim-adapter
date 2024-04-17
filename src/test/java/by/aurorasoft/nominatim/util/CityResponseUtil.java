package by.aurorasoft.nominatim.util;

import by.aurorasoft.nominatim.controller.city.model.CityResponse;
import lombok.experimental.UtilityClass;
import org.wololo.jts2geojson.GeoJSONReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class CityResponseUtil {

    public static void checkEquals(final CityResponse expected,
                                   final CityResponse actual,
                                   final GeoJSONReader geoJSONReader) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        GeometryUtil.checkEquals(expected.getGeometry(), actual.getGeometry(), geoJSONReader);
        assertSame(expected.getType(), actual.getType());
    }
}
