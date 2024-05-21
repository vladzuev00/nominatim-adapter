package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.factory;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.cache.GeometryCache;
import by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.preparer.GeometryPreparer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class GeometryCacheFactoryTest {

    @Mock
    private GeometryPreparer mockedGeometryPreparer;

    private boolean streamWasClosed;

    @Before
    public void resetStreamWasClosed() {
        streamWasClosed = false;
    }

    @Test
    public void cacheShouldBeCreated() {
        final CityGeometry firstGivenCityGeometry = mock(CityGeometry.class);
        final CityGeometry secondGivenCityGeometry = mock(CityGeometry.class);
        final Geometry givenScannedLocation = mock(Geometry.class);
        final GeometryCacheFactory givenFactory = createFactory(
                givenScannedLocation,
                firstGivenCityGeometry,
                secondGivenCityGeometry
        );

        final PreparedCityGeometry firstGivenPreparedCityGeometry = mockPreparedGeometryFor(firstGivenCityGeometry);
        final PreparedCityGeometry secondGivenPreparedCityGeometry = mockPreparedGeometryFor(secondGivenCityGeometry);
        final PreparedGeometry givenPreparedScannedLocation = mockPreparedGeometryFor(givenScannedLocation);

        final GeometryCache actual = givenFactory.create();
        final GeometryCache expected = new GeometryCache(
                Set.of(firstGivenPreparedCityGeometry, secondGivenPreparedCityGeometry),
                givenPreparedScannedLocation
        );
        checkEquals(expected, actual);

        assertTrue(streamWasClosed);
    }

    private GeometryCacheFactory createFactory(final Geometry scannedGeometry, final CityGeometry... cityGeometries) {
        final Stream<CityGeometry> streamCapturingClose = createStreamCapturingClose(cityGeometries);
        return new TestGeometryCacheFactory(mockedGeometryPreparer, streamCapturingClose, scannedGeometry);
    }

    private Stream<CityGeometry> createStreamCapturingClose(final CityGeometry... geometries) {
        return Stream.of(geometries).onClose(() -> streamWasClosed = true);
    }

    private PreparedCityGeometry mockPreparedGeometryFor(final CityGeometry geometry) {
        final PreparedCityGeometry preparedGeometry = mock(PreparedCityGeometry.class);
        when(mockedGeometryPreparer.prepare(same(geometry))).thenReturn(preparedGeometry);
        return preparedGeometry;
    }

    private PreparedGeometry mockPreparedGeometryFor(final Geometry geometry) {
        final PreparedGeometry preparedGeometry = mock(PreparedGeometry.class);
        when(mockedGeometryPreparer.prepare(same(geometry))).thenReturn(preparedGeometry);
        return preparedGeometry;
    }

    private void checkEquals(final GeometryCache expected, final GeometryCache actual) {
        assertEquals(expected.getCityGeometries(), actual.getCityGeometries());
        assertSame(expected.getScannedGeometry(), actual.getScannedGeometry());
    }

    private static final class TestGeometryCacheFactory extends GeometryCacheFactory {
        private final Stream<CityGeometry> cityGeometries;
        private final Geometry scannedGeometry;

        public TestGeometryCacheFactory(final GeometryPreparer geometryPreparer,
                                        final Stream<CityGeometry> cityGeometries,
                                        final Geometry scannedGeometry) {
            super(geometryPreparer);
            this.cityGeometries = cityGeometries;
            this.scannedGeometry = scannedGeometry;
        }

        @Override
        protected Stream<CityGeometry> getCityGeometries() {
            return cityGeometries;
        }

        @Override
        protected Geometry getScannedGeometry() {
            return scannedGeometry;
        }
    }
}
