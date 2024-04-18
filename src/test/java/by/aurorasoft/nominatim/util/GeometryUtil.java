package by.aurorasoft.nominatim.util;

import lombok.experimental.UtilityClass;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Polygon;
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

    public static Polygon createPolygonByText(final String text, final GeometryFactory geometryFactory) {
        return createGeometryByText(text, geometryFactory, Polygon.class);
    }

    public static LineString createLineByText(final String text, final GeometryFactory geometryFactory) {
        return createGeometryByText(text, geometryFactory, LineString.class);
    }

    private static boolean areEquals(final org.wololo.geojson.Geometry expected,
                                     final org.wololo.geojson.Geometry actual,
                                     final GeoJSONReader geoJSONReader) {
        return expected == actual || Objects.equals(geoJSONReader.read(expected), geoJSONReader.read(actual));
    }

    private static <T extends Geometry> T createGeometryByText(final String text,
                                                               final GeometryFactory geometryFactory,
                                                               final Class<T> geometryType) {
        try {
            final Geometry geometry = new WKTReader(geometryFactory).read(text);
            return geometryType.cast(geometry);
        } catch (final ParseException cause) {
            throw new RuntimeException(cause);
        }
    }
}
