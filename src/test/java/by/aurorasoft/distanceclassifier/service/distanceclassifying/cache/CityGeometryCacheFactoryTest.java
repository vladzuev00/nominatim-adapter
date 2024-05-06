package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.geometrypreparer.CityGeometryPreparer;
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
    private CityGeometryPreparer mockedGeometryPreparer;

    private boolean streamWasClosed;

    @Before
    public void resetStreamWasClosed() {
        streamWasClosed = false;
    }

    @Test
    public void cacheShouldBeCreatedWithLoadedGeometries() {
        final CityGeometryCacheFactory givenFactory = createFactory(true);

        final CityGeometry firstGivenGeometry = mock(CityGeometry.class);
        final CityGeometry secondGivenGeometry = mock(CityGeometry.class);
        final Stream<CityGeometry> givenGeometries = createStreamCapturingClose(firstGivenGeometry, secondGivenGeometry);
        when(mockedCityService.findCityGeometries()).thenReturn(givenGeometries);

        final PreparedCityGeometry firstGivenPreparedGeometry = mockPreparedGeometryFor(firstGivenGeometry);
        final PreparedCityGeometry secondGivenPreparedGeometry = mockPreparedGeometryFor(secondGivenGeometry);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final Set<PreparedCityGeometry> actualGeometries = actual.getGeometries();
        final Set<PreparedCityGeometry> expectedGeometries = Set.of(
                firstGivenPreparedGeometry,
                secondGivenPreparedGeometry
        );
        assertEquals(expectedGeometries, actualGeometries);

        assertTrue(streamWasClosed);
    }

    @Test
    public void cacheShouldBeCreatedWithoutLoadedGeometries() {
        final CityGeometryCacheFactory givenFactory = createFactory(false);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final Set<PreparedCityGeometry> actualGeometries = actual.getGeometries();
        assertTrue(actualGeometries.isEmpty());

        verifyNoInteractions(mockedCityService, mockedGeometryPreparer);
    }

    private CityGeometryCacheFactory createFactory(final boolean shouldBeFilled) {
        return new CityGeometryCacheFactory(mockedCityService, mockedGeometryPreparer, shouldBeFilled);
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
