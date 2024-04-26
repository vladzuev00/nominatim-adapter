package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
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

        final var actualGeometriesByBoundingBoxes = actual.getBoundedGeometries();
        assertSame(givenGeometriesByBoundingBoxes, actualGeometriesByBoundingBoxes);
    }

    @Test
    public void cacheShouldBeCreatedWithoutLoadedGeometries() {
        final CityGeometryCacheFactory givenFactory = createFactory(false);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final var actualGeometriesByBoundingBoxes = actual.getBoundedGeometries();
        assertTrue(actualGeometriesByBoundingBoxes.isEmpty());

        verifyNoInteractions(mockedCityService);
    }

    private CityGeometryCacheFactory createFactory(final boolean shouldBeFilled) {
        return new CityGeometryCacheFactory(mockedCityService, shouldBeFilled);
    }
}
