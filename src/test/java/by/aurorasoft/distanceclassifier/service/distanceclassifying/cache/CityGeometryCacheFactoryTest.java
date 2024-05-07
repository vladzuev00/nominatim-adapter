package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.CityMapCache;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.cache.CityMapCacheFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class CityGeometryCacheFactoryTest {

    @Mock
    private CityService mockedCityService;

    @Mock
    private GeometryPreparer mockedGeometryPreparer;

    private boolean streamWasClosed;

    @Before
    public void resetStreamWasClosed() {
        streamWasClosed = false;
    }

    @Test
    public void cacheShouldBeCreatedWithLoadedGeometries() {
        final CityMapCacheFactory givenFactory = createFactory(true);

        final CityGeometry firstGivenGeometry = mock(CityGeometry.class);
        final CityGeometry secondGivenGeometry = mock(CityGeometry.class);
        final Stream<CityGeometry> givenGeometries = createStreamCapturingClose(firstGivenGeometry, secondGivenGeometry);
        when(mockedCityService.findCityGeometries()).thenReturn(givenGeometries);

        final PreparedCityGeometry firstGivenPreparedGeometry = mockPreparedGeometryFor(firstGivenGeometry);
        final PreparedCityGeometry secondGivenPreparedGeometry = mockPreparedGeometryFor(secondGivenGeometry);

        final CityMapCache actual = givenFactory.create();
        assertNotNull(actual);

        final Set<PreparedCityGeometry> actualGeometries = actual.getMap();
        final Set<PreparedCityGeometry> expectedGeometries = Set.of(
                firstGivenPreparedGeometry,
                secondGivenPreparedGeometry
        );
        assertEquals(expectedGeometries, actualGeometries);

        assertTrue(streamWasClosed);
    }

    @Test
    public void cacheShouldBeCreatedWithoutLoadedGeometries() {
        final CityMapCacheFactory givenFactory = createFactory(false);

        final CityMapCache actual = givenFactory.create();
        assertNotNull(actual);

        final Set<PreparedCityGeometry> actualGeometries = actual.getMap();
        assertTrue(actualGeometries.isEmpty());

        verifyNoInteractions(mockedCityService, mockedGeometryPreparer);
    }

    private CityMapCacheFactory createFactory(final boolean shouldBeFilled) {
        return new CityMapCacheFactory(mockedCityService, mockedGeometryPreparer, shouldBeFilled);
    }

    private PreparedCityGeometry mockPreparedGeometryFor(final CityGeometry geometry) {
        final PreparedCityGeometry preparedGeometry = mock(PreparedCityGeometry.class);
        when(mockedGeometryPreparer.prepare(same(geometry))).thenReturn(preparedGeometry);
        return preparedGeometry;
    }

    private Stream<CityGeometry> createStreamCapturingClose(final CityGeometry... geometries) {
        return Stream.of(geometries).onClose(() -> streamWasClosed = true);
    }
}
