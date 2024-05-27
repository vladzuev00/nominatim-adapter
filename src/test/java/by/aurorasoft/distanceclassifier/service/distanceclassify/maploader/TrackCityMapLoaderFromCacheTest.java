package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.GeometryCache;
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

        final PreparedGeometry firstGivenGeometry = mock(PreparedGeometry.class);
        final PreparedGeometry secondGivenGeometry = mock(PreparedGeometry.class);
        final PreparedGeometry thirdGivenGeometry = mock(PreparedGeometry.class);
        final PreparedCityGeometry firstGivenCityGeometry = createCityGeometry(firstGivenGeometry, givenLine, true);
        final PreparedCityGeometry secondGivenCityGeometry = createCityGeometry(secondGivenGeometry, givenLine, true);
        final PreparedCityGeometry thirdGivenCityGeometry = createCityGeometry(thirdGivenGeometry, givenLine, false);
        putGeometriesInCache(firstGivenCityGeometry, secondGivenCityGeometry, thirdGivenCityGeometry);

        final Stream<PreparedGeometry> actual = loader.loadCityGeometries(givenLine);
        final Set<PreparedGeometry> actualAsSet = actual.collect(toUnmodifiableSet());
        final Set<PreparedGeometry> expectedAsSet = Set.of(firstGivenGeometry, secondGivenGeometry);
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void scannedGeometryShouldBeLoaded() {
        final PreparedGeometry givenScannedGeometry = mock(PreparedGeometry.class);
        when(mockedCache.getScannedGeometry()).thenReturn(givenScannedGeometry);

        final PreparedGeometry actual = loader.loadScannedGeometry();
        assertSame(givenScannedGeometry, actual);
    }

    private void putGeometriesInCache(final PreparedCityGeometry... geometries) {
        when(mockedCache.getCityGeometries()).thenReturn(Set.of(geometries));
    }

    private PreparedCityGeometry createCityGeometry(final PreparedGeometry geometry,
                                                    final LineString line,
                                                    final boolean lineIntersected) {
        final PreparedGeometry boundingBox = mock(PreparedGeometry.class);
        when(boundingBox.intersects(same(line))).thenReturn(lineIntersected);
        return new PreparedCityGeometry(geometry, boundingBox);
    }
}
