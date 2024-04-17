package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class GeometryUtil {

    public static void checkEquals(final Geometry expected, final Geometry actual, final GeoJSONReader geoJSONReader) {
        assertEquals(geoJSONReader.read(expected), geoJSONReader.read(actual));
    }
}
