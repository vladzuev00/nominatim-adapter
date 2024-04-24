package by.aurorasoft.mileagecalculator.config;

import org.junit.Test;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import static org.junit.Assert.assertNotNull;

public final class GeoJsonConfigTest {
    private final GeoJsonConfig config = new GeoJsonConfig();

    @Test
    public void geoJsonReaderShouldBeCreated() {
        final GeoJSONReader actual = config.geoJsonReader();
        assertNotNull(actual);
    }

    @Test
    public void geoJsonWriterShouldBeCreated() {
        final GeoJSONWriter actual = config.geoJsonWriter();
        assertNotNull(actual);
    }
}
