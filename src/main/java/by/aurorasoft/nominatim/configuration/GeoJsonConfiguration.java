package by.aurorasoft.nominatim.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wololo.jts2geojson.GeoJSONReader;

@Configuration
public class GeoJsonConfiguration {

    @Bean
    public GeoJSONReader geoJsonReader() {
        return new GeoJSONReader();
    }
}
