package by.aurorasoft.distanceclassifier.service.distanceclassifying.cache;

import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.model.BoundedPreparedGeometry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

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

        final Set<BoundedPreparedGeometry> givenGeometries = mock(Set.class);
        when(mockedCityService.findBoundedPreparedGeometries()).thenReturn(givenGeometries);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final Set<BoundedPreparedGeometry> actualGeometries = actual.getGeometries();
        assertSame(givenGeometries, actualGeometries);
    }

    @Test
    public void cacheShouldBeCreatedWithoutLoadedGeometries() {
        final CityGeometryCacheFactory givenFactory = createFactory(false);

        final CityGeometryCache actual = givenFactory.create();
        assertNotNull(actual);

        final Set<BoundedPreparedGeometry> actualGeometries = actual.getGeometries();
        assertTrue(actualGeometries.isEmpty());

        verifyNoInteractions(mockedCityService);
    }

    private CityGeometryCacheFactory createFactory(final boolean shouldBeFilled) {
        return new CityGeometryCacheFactory(mockedCityService, shouldBeFilled);
    }
}
