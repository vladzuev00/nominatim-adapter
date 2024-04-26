package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCacheFactory;
import by.nhorushko.trackfilter.TrackFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassifyingDistanceConfig {

    @Bean
    public TrackFilter trackFilter() {
        return new TrackFilter();
    }

    @Bean
    public CityGeometryCache cityGeometryCache(final CityGeometryCacheFactory factory) {
        return factory.create();
    }
}
