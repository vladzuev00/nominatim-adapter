package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCacheFactory;
import by.nhorushko.trackfilter.TrackFilterI;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ClassifyingDistanceConfigTest {
    private final ClassifyingDistanceConfig config = new ClassifyingDistanceConfig();

    @Test
    public void trackFilterShouldBeCreated() {
        final TrackFilterI actual = config.trackFilter();
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
