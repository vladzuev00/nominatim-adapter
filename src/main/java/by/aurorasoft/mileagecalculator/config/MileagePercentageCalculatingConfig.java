package by.aurorasoft.mileagecalculator.config;

import by.aurorasoft.mileagecalculator.service.mileage.cache.CityGeometryCache;
import by.aurorasoft.mileagecalculator.service.mileage.cache.CityGeometryCacheFactory;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorImpl;
import by.nhorushko.trackfilter.TrackFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MileagePercentageCalculatingConfig {

    @Bean
    public TrackFilter trackFilter() {
        return new TrackFilter();
    }

    @Bean
    public DistanceCalculator distanceCalculator() {
        return new DistanceCalculatorImpl();
    }

    @Bean
    public CityGeometryCache cityGeometryCache(final CityGeometryCacheFactory factory) {
        return factory.create();
    }
}
