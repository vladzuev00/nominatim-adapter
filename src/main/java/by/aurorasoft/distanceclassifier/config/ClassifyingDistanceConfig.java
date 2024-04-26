package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCacheFactory;
import by.nhorushko.trackfilter.TrackFilter;
import by.nhorushko.trackfilter.TrackFilterI;
import by.nhorushko.trackfilter.TrackFilterImpr;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassifyingDistanceConfig {

    @Bean
    public TrackFilterI trackFilter() {
        return new TrackFilterImpr();
    }

    @Bean
    public CityGeometryCache cityGeometryCache(final CityGeometryCacheFactory factory) {
        return factory.create();
    }
}
