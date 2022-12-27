package by.aurorasoft.nominatim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wololo.jts2geojson.GeoJSONReader;

@Configuration
public class GeoJsonConfig {

    @Bean
    public GeoJSONReader geoJsonReader() {
        return new GeoJSONReader();
    }
}
