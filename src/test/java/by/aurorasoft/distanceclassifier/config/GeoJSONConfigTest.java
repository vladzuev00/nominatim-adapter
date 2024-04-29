package by.aurorasoft.distanceclassifier.config;

import org.junit.Test;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static org.junit.Assert.assertNotNull;

public final class GeoJSONConfigTest {
    private final GeoJSONConfig config = new GeoJSONConfig();

    @Test
    public void geoJSONReaderShouldBeCreated() {
        final GeoJSONReader actual = config.geoJSONReader();
        assertNotNull(actual);
    }

    @Test
    public void geoJSONWriterShouldBeCreated() {
        final GeoJSONWriter actual = config.geoJSONWriter();
        assertNotNull(actual);
    }
}
