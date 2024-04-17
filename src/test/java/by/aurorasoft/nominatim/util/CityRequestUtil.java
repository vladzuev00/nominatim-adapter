package by.aurorasoft.nominatim.util;

import by.aurorasoft.nominatim.controller.city.model.CityRequest;
import lombok.experimental.UtilityClass;
import org.wololo.jts2geojson.GeoJSONReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class CityRequestUtil {

    public static void checkEquals(final CityRequest expected, final CityRequest actual, final GeoJSONReader geoJSONReader) {
        assertEquals(expected.getName(), actual.getName());
        GeometryUtil.checkEquals(expected.getGeometry(), actual.getGeometry(), geoJSONReader);
        assertSame(expected.getType(), actual.getType());
    }
}
