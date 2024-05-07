package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.CityMapCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackCityGeometryLoaderFromCacheTest {

    @Mock
    private CityMapCache mockedCache;

    private TrackCityGeometryLoaderFromCache loader;

    @Before
    public void initializeLoader() {
        loader = new TrackCityGeometryLoaderFromCache(null, null, mockedCache);
    }

    @Test
    public void geometriesShouldBeLoadedInternally() {
        final LineString givenLine = mock(LineString.class);

        final PreparedCityGeometry firstGivenGeometry = createGeometry(givenLine, true);
        final PreparedCityGeometry secondGivenGeometry = createGeometry(givenLine, true);
        final Set<PreparedCityGeometry> givenGeometries = Set.of(
                firstGivenGeometry,
                secondGivenGeometry,
                createGeometry(givenLine, false)
        );
        when(mockedCache.getMap()).thenReturn(givenGeometries);

        final Set<PreparedCityGeometry> actual = loader.loadInternal(givenLine);
        final Set<PreparedCityGeometry> expected = Set.of(firstGivenGeometry, secondGivenGeometry);
        assertEquals(expected, actual);
    }

    private static PreparedCityGeometry createGeometry(final LineString line, final boolean lineIntersected) {
        final PreparedGeometry geometry = mock(PreparedGeometry.class);
        final PreparedGeometry boundingBox = mock(PreparedGeometry.class);
        when(boundingBox.intersects(same(line))).thenReturn(lineIntersected);
        return new PreparedCityGeometry(geometry, boundingBox);
    }
}
