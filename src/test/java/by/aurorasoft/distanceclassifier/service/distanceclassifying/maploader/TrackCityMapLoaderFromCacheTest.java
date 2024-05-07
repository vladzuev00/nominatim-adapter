package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.GeometryCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackCityMapLoaderFromCacheTest {

    @Mock
    private GeometryCache mockedCache;

    private TrackCityMapLoaderFromCache loader;

    @Before
    public void initializeLoader() {
        loader = new TrackCityMapLoaderFromCache(null, null, mockedCache);
    }

    @Test
    public void geometriesShouldBeLoaded() {
        final LineString givenLine = mock(LineString.class);

        final PreparedCityGeometry firstGivenGeometry = createGeometry(givenLine, true);
        final PreparedCityGeometry secondGivenGeometry = createGeometry(givenLine, true);
        final Set<PreparedCityGeometry> givenGeometries = Set.of(
                firstGivenGeometry,
                secondGivenGeometry,
                createGeometry(givenLine, false)
        );
        when(mockedCache.getCityGeometries()).thenReturn(givenGeometries);

        final Stream<PreparedCityGeometry> actual = loader.loadCityGeometries(givenLine);
        final Set<PreparedCityGeometry> actualAsSet = actual.collect(toUnmodifiableSet());
        final Set<PreparedCityGeometry> expectedAsSet = Set.of(firstGivenGeometry, secondGivenGeometry);
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void scannedGeometryShouldBeLoaded() {
        final PreparedGeometry givenScannedGeometry = mock(PreparedGeometry.class);
        when(mockedCache.getScannedGeometry()).thenReturn(givenScannedGeometry);

        final PreparedGeometry actual = loader.loadScannedGeometry();
        assertSame(givenScannedGeometry, actual);
    }

    private static PreparedCityGeometry createGeometry(final LineString line, final boolean lineIntersected) {
        final PreparedGeometry geometry = mock(PreparedGeometry.class);
        final PreparedGeometry boundingBox = mock(PreparedGeometry.class);
        when(boundingBox.intersects(same(line))).thenReturn(lineIntersected);
        return new PreparedCityGeometry(geometry, boundingBox);
    }
}
