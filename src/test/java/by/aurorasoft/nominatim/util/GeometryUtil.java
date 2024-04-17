package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;

import java.util.Objects;

import static org.junit.Assert.assertTrue;

@UtilityClass
public final class GeometryUtil {

    public static void checkEquals(final Geometry expected, final Geometry actual, final GeoJSONReader geoJSONReader) {
        assertTrue(areEquals(expected, actual, geoJSONReader));
    }

    private static boolean areEquals(final Geometry expected, final Geometry actual, final GeoJSONReader geoJSONReader) {
        return expected == actual || Objects.equals(geoJSONReader.read(expected), geoJSONReader.read(actual));
    }
}
