package by.aurorasoft.mileagecalculator.service.mileage.loader;

import by.aurorasoft.mileagecalculator.service.mileage.cache.CityGeometryCache;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public void geometriesShouldBeLoaded() {
        final LineString givenLine = mock(LineString.class);

        final PreparedGeometry firstGivenBoundingBox = createBoundingBox(givenLine, true);
        final PreparedGeometry firstGivenGeometry = mock(PreparedGeometry.class);
        final PreparedGeometry secondGivenBoundingBox = createBoundingBox(givenLine, true);
        final PreparedGeometry secondGivenGeometry = mock(PreparedGeometry.class);
        final PreparedGeometry thirdGivenBoundingBox = createBoundingBox(givenLine, false);
        final PreparedGeometry thirdGivenGeometry = mock(PreparedGeometry.class);
        final Map<PreparedGeometry, PreparedGeometry> givenGeometriesByBoundingBoxes = new LinkedHashMap<>() {
            {
                put(firstGivenBoundingBox, firstGivenGeometry);
                put(secondGivenBoundingBox, secondGivenGeometry);
                put(thirdGivenBoundingBox, thirdGivenGeometry);
            }
        };
        when(mockedCache.getGeometriesByBoundingBoxes()).thenReturn(givenGeometriesByBoundingBoxes);

        final List<PreparedGeometry> actual = loader.load(givenLine);
        final List<PreparedGeometry> expected = List.of(firstGivenGeometry, secondGivenGeometry);
        assertEquals(expected, actual);
    }

    private static PreparedGeometry createBoundingBox(final LineString line, final boolean intersected) {
        final PreparedGeometry boundingBox = mock(PreparedGeometry.class);
        when(boundingBox.intersects(same(line))).thenReturn(intersected);
        return boundingBox;
    }
}
