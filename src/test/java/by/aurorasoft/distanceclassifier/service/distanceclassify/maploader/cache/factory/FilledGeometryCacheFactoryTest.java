package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.model.dto.ScannedLocation;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.stream.Stream;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class FilledGeometryCacheFactoryTest {

    @Mock
    private CityService mockedCityService;

    @Mock
    private ScannedLocationService mockedScannedLocationService;

    private FilledGeometryCacheFactory factory;

    @Before
    public void initializeFactory() {
        factory = new FilledGeometryCacheFactory(null, mockedCityService, mockedScannedLocationService);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void cityGeometriesShouldBeGot() {
        final Stream<CityGeometry> givenGeometries = mock(Stream.class);
        when(mockedCityService.findCityGeometries()).thenReturn(givenGeometries);

        final Stream<CityGeometry> actual = factory.getCityGeometries();
        assertSame(givenGeometries, actual);
    }

    @Test
    public void scannedGeometryShouldBeGot() {
        final Geometry givenGeometry = mock(Geometry.class);
        final ScannedLocation givenLocation = createScannedLocation(givenGeometry);
        when(mockedScannedLocationService.get()).thenReturn(givenLocation);

        final Geometry actual = factory.getScannedGeometry();
        assertSame(givenGeometry, actual);
    }

    private ScannedLocation createScannedLocation(final Geometry geometry) {
        return ScannedLocation.builder()
                .geometry(geometry)
                .build();
    }
}
