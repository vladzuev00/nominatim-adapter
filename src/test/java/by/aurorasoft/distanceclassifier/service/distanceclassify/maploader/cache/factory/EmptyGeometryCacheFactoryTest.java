package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.stream.Stream;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class EmptyGeometryCacheFactoryTest {

    @Mock
    private GeometryService mockedGeometryService;

    private EmptyGeometryCacheFactory factory;

    @Before
    public void initializeFactory() {
        factory = new EmptyGeometryCacheFactory(null, mockedGeometryService);
    }

    @Test
    public void cityGeometriesShouldBeGot() {
        final Stream<CityGeometry> actual = factory.getCityGeometries();

        final boolean actualEmpty = actual.findAny().isEmpty();
        assertTrue(actualEmpty);
    }

    @Test
    public void scannedGeometryShouldBeGot() {
        final Polygon givenGeometry = mock(Polygon.class);
        when(mockedGeometryService.createEmptyPolygon()).thenReturn(givenGeometry);

        final Geometry actual = factory.getScannedGeometry();
        assertSame(givenGeometry, actual);
    }
}
