package by.aurorasoft.distanceclassifier.testutil;

import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse.CityGeometryResponse;
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
        checkEquals(expected.getGeometry(), actual.getGeometry(), geoJSONReader);
        assertSame(expected.getType(), actual.getType());
    }


    public static void checkEquals(final CityGeometryResponse expected,
                                   final CityGeometryResponse actual,
                                   final GeoJSONReader geoJSONReader) {
        GeometryUtil.checkEquals(expected.getGeometry(), actual.getGeometry(), geoJSONReader);
        GeometryUtil.checkEquals(expected.getBoundingBox(), actual.getBoundingBox(), geoJSONReader);
    }
}
