package by.aurorasoft.distanceclassifier.service.distanceclassifying.loader;

import by.aurorasoft.distanceclassifier.model.BoundedPreparedGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.cache.CityGeometryCache;
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
    private CityGeometryCache mockedCache;

    private TrackCityGeometryLoaderFromCache loader;

    @Before
    public void initializeLoader() {
        loader = new TrackCityGeometryLoaderFromCache(null, null, mockedCache);
    }

    @Test
    public void geometriesShouldBeLoadedInternally() {
        final LineString givenLine = mock(LineString.class);

        final BoundedPreparedGeometry firstGivenGeometry = createGeometry(givenLine, true);
        final BoundedPreparedGeometry secondGivenGeometry = createGeometry(givenLine, true);
        final Set<BoundedPreparedGeometry> givenGeometries = Set.of(
                firstGivenGeometry,
                secondGivenGeometry,
                createGeometry(givenLine, false)
        );
        when(mockedCache.getGeometries()).thenReturn(givenGeometries);

        final Set<BoundedPreparedGeometry> actual = loader.loadInternal(givenLine);
        final Set<BoundedPreparedGeometry> expected = Set.of(firstGivenGeometry, secondGivenGeometry);
        assertEquals(expected, actual);
    }

    private static BoundedPreparedGeometry createGeometry(final LineString line, final boolean lineIntersected) {
        final PreparedGeometry geometry = mock(PreparedGeometry.class);
        final PreparedGeometry boundingBox = mock(PreparedGeometry.class);
        when(boundingBox.intersects(same(line))).thenReturn(lineIntersected);
        return new BoundedPreparedGeometry(geometry, boundingBox);
    }
}
