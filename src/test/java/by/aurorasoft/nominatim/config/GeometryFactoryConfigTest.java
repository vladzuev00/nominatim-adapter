package by.aurorasoft.nominatim.config;

import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

import static org.junit.Assert.assertEquals;

public final class GeometryFactoryConfigTest {
    private final GeometryFactoryConfig config = new GeometryFactoryConfig();

    @Test
    public void geometryFactoryShouldBeCreated() {
        final GeometryFactory actual = config.geometryFactory();
        final GeometryFactory expected = new GeometryFactory(new PrecisionModel(), 4326);
        checkEquals(expected, actual);
    }

    @Test
    public void precisionModelShouldBeCreated() {
        final PrecisionModel actual = config.precisionModel();
        final PrecisionModel expected = new PrecisionModel();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final GeometryFactory expected, final GeometryFactory actual) {
        assertEquals(expected.getPrecisionModel(), actual.getPrecisionModel());
        assertEquals(expected.getSRID(), actual.getSRID());
    }
}
