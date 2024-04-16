package by.aurorasoft.nominatim.config;

import by.aurorasoft.nominatim.service.mileage.cache.CityGeometryCache;
import by.aurorasoft.nominatim.service.mileage.cache.CityGeometryCacheFactory;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.trackfilter.TrackFilter;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class MileageConfigTest {
    private final MileageConfig config = new MileageConfig();

    @Test
    public void trackFilterShouldBeCreated() {
        final TrackFilter actual = config.trackFilter();
        assertNotNull(actual);
    }

    @Test
    public void distanceCalculatorShouldBeCreated() {
        final DistanceCalculator actual = config.distanceCalculator();
        assertNotNull(actual);
    }

    @Test
    public void cityGeometryCacheShouldBeCreated() {
        final CityGeometryCacheFactory givenFactory = mock(CityGeometryCacheFactory.class);

        final CityGeometryCache givenCache = mock(CityGeometryCache.class);
        when(givenFactory.create()).thenReturn(givenCache);

        final CityGeometryCache actual = config.cityGeometryCache(givenFactory);
        assertSame(givenCache, actual);
    }
}
