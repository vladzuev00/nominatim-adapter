package by.aurorasoft.distanceclassifier.testutil;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.wololo.jts2geojson.GeoJSONReader;

import java.util.Objects;

import static org.junit.Assert.assertTrue;

@UtilityClass
public final class GeometryUtil {

    public static void checkEquals(final org.wololo.geojson.Geometry expected,
                                   final org.wololo.geojson.Geometry actual,
                                   final GeoJSONReader geoJSONReader) {
        assertTrue(areEquals(expected, actual, geoJSONReader));
    }

    public static Geometry createGeometry(final String text, final GeometryFactory factory) {
        return createGeometry(text, factory, Geometry.class);
    }

    public static Polygon createPolygon(final String text, final GeometryFactory factory) {
        return createGeometry(text, factory, Polygon.class);
    }

    public static LineString createLine(final String text, final GeometryFactory factory) {
        return createGeometry(text, factory, LineString.class);
    }

    public static MultiPolygon createMultipolygon(final String text, final GeometryFactory factory) {
        return createGeometry(text, factory, MultiPolygon.class);
    }

    private static boolean areEquals(final org.wololo.geojson.Geometry expected,
                                     final org.wololo.geojson.Geometry actual,
                                     final GeoJSONReader geoJSONReader) {
        return expected == actual || Objects.equals(geoJSONReader.read(expected), geoJSONReader.read(actual));
    }

    private static <T extends Geometry> T createGeometry(final String text,
                                                         final GeometryFactory factory,
                                                         final Class<T> type) {
        try {
            final Geometry geometry = new WKTReader(factory).read(text);
            return type.cast(geometry);
        } catch (final ParseException cause) {
            throw new RuntimeException(cause);
        }
    }
}
