package by.aurorasoft.distanceclassifier.testutil;

import by.aurorasoft.distanceclassifier.controller.city.model.CityResponse;
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
        GeometryUtil.checkEquals(expected.getGeometry().getGeometry(), actual.getGeometry().getGeometry(), geoJSONReader);
        GeometryUtil.checkEquals(expected.getGeometry().getBoundingBox(), actual.getGeometry().getBoundingBox(), geoJSONReader);
        assertSame(expected.getType(), actual.getType());
    }
}
