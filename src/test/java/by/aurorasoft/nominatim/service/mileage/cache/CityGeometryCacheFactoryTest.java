package by.aurorasoft.nominatim.service.mileage.cache;

import by.aurorasoft.nominatim.crud.service.CityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class CityGeometryCacheFactoryTest {

    @Mock
    private CityService mockedCityService;

    @Test
    @SuppressWarnings("unchecked")
    public void cacheShouldBeCreatedWithLoadedGeometries() {
        final CityGeometryCacheFactory givenFactory = createFactory(true);

        final Map<PreparedGeometry, PreparedGeometry> givenGeometriesByBoundingBoxes = mock(Map.class);
        when(mockedCityService.findPreparedGeometriesByPreparedBoundingBoxes())
                .thenReturn(givenGeometriesByBoundingBoxes);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final var actualGeometriesByBoundingBoxes = actual.getGeometriesByBoundingBoxes();
        assertSame(givenGeometriesByBoundingBoxes, actualGeometriesByBoundingBoxes);
    }

    @Test
    public void cacheShouldBeCreatedWithoutLoadedGeometries() {
        final CityGeometryCacheFactory givenFactory = createFactory(false);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final var actualGeometriesByBoundingBoxes = actual.getGeometriesByBoundingBoxes();
        assertTrue(actualGeometriesByBoundingBoxes.isEmpty());

        verifyNoInteractions(mockedCityService);
    }

    private CityGeometryCacheFactory createFactory(final boolean shouldBeCached) {
        return new CityGeometryCacheFactory(mockedCityService, shouldBeCached);
    }
}
