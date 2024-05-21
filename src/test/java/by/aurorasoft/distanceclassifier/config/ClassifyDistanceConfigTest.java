package by.aurorasoft.distanceclassifier.config;

import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.GeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory.GeometryCacheFactory;
import by.nhorushko.trackfilter.TrackFilterI;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ClassifyDistanceConfigTest {
    private final ClassifyDistanceConfig config = new ClassifyDistanceConfig();

    @Test
    public void trackFilterShouldBeCreated() {
        final TrackFilterI actual = config.trackFilter();
        assertNotNull(actual);
    }

    @Test
    public void geometryCacheShouldBeCreated() {
        final GeometryCacheFactory givenFactory = mock(GeometryCacheFactory.class);

        final GeometryCache givenCache = mock(GeometryCache.class);
        when(givenFactory.create()).thenReturn(givenCache);

        final GeometryCache actual = config.geometryCache(givenFactory);
        assertSame(givenCache, actual);
    }
}
