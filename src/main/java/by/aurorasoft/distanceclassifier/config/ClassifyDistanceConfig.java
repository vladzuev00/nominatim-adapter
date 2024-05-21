package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.GeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.factory.GeometryCacheFactory;
import by.nhorushko.trackfilter.TrackFilterI;
import by.nhorushko.trackfilter.TrackFilterImpr;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClassifyDistanceConfig {

    @Bean
    public TrackFilterI trackFilter() {
        return new TrackFilterImpr();
    }

    @Bean
    public GeometryCache geometryCache(final GeometryCacheFactory factory) {
        return factory.create();
    }
}
