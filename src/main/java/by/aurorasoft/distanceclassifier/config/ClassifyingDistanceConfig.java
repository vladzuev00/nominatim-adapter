package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.CityMapCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.CityMapCacheFactory;
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
    public CityMapCache cityGeometryCache(final CityMapCacheFactory factory) {
        return factory.create();
    }
}
