package by.aurorasoft.nominatim.service.mileage.cache;

import by.aurorasoft.nominatim.crud.service.CityService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class CityGeometryCacheFactoryTest {

    @Mock
    private CityService mockedCityService;

    private CityGeometryCacheFactory factory;

    @Before
    public void initializeFactory() {
        factory = new CityGeometryCacheFactory(mockedCityService);
    }

    @Test
    public void cacheShouldBeCreated() {
        final Map<PreparedGeometry, PreparedGeometry> givenGeometriesByBoundingBoxes = new HashMap<>();
        when(mockedCityService.findPreparedGeometriesByPreparedBoundingBoxes())
                .thenReturn(givenGeometriesByBoundingBoxes);

        final CityGeometryCache actual = factory.create();
        assertNotNull(actual);

        final var actualGeometriesByBoundingBoxes = actual.getGeometriesByBoundingBoxes();
        assertSame(givenGeometriesByBoundingBoxes, actualGeometriesByBoundingBoxes);
    }
}
