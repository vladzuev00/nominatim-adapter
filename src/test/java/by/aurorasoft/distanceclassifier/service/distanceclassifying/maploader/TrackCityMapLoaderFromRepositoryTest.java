package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;

import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
import by.aurorasoft.distanceclassifier.crud.model.dto.ScannedLocation;
import by.aurorasoft.distanceclassifier.crud.service.CityService;
import by.aurorasoft.distanceclassifier.crud.service.ScannedLocationService;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackCityMapLoaderFromRepositoryTest {

    @Mock
    private CityService mockedCityService;

    @Mock
    private ScannedLocationService mockedScannedLocationService;

    @Mock
    private GeometryPreparer mockedGeometryPreparer;

    private TrackCityMapLoaderFromRepository loader;

    @Before
    public void initializeLoader() {
        loader = new TrackCityMapLoaderFromRepository(
                null,
                null,
                mockedCityService,
                mockedScannedLocationService,
                mockedGeometryPreparer
        );
    }

    @Test
    public void geometriesShouldBeLoaded() {
        final LineString givenLine = mock(LineString.class);

        final Geometry firstGivenGeometry = mock(Geometry.class);
        final Geometry secondGivenGeometry = mock(Geometry.class);
        mockIntersectedCityGeometries(givenLine, firstGivenGeometry, secondGivenGeometry);

        final PreparedGeometry firstGivenPreparedGeometry = mockPreparedGeometryFor(firstGivenGeometry);
        final PreparedGeometry secondGivenPreparedGeometry = mockPreparedGeometryFor(secondGivenGeometry);

        final Stream<PreparedGeometry> actual = loader.loadCityGeometries(givenLine);
        final Set<PreparedGeometry> actualAsSet = actual.collect(toUnmodifiableSet());
        final Set<PreparedGeometry> expectedAsSet = Set.of(firstGivenPreparedGeometry, secondGivenPreparedGeometry);
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void scannedGeometryShouldBeLoaded() {
        final Geometry givenGeometry = mock(Geometry.class);
        final ScannedLocation givenScannedLocation = createScannedLocation(givenGeometry);
        when(mockedScannedLocationService.get()).thenReturn(givenScannedLocation);

        final PreparedGeometry givenPreparedGeometry = mockPreparedGeometryFor(givenGeometry);

        final PreparedGeometry actual = loader.loadScannedGeometry();
        assertSame(givenPreparedGeometry, actual);
    }

    private void mockIntersectedCityGeometries(final LineString line, final Geometry... geometries) {
        final Stream<CityGeometry> cityGeometries = stream(geometries).map(this::createCityGeometry);
        when(mockedCityService.findIntersectedCityGeometries(same(line))).thenReturn(cityGeometries);
    }

    private CityGeometry createCityGeometry(final Geometry geometry) {
        return CityGeometry.builder()
                .geometry(geometry)
                .build();
    }

    private PreparedGeometry mockPreparedGeometryFor(final Geometry geometry) {
        final PreparedGeometry preparedGeometry = mock(PreparedGeometry.class);
        when(mockedGeometryPreparer.prepare(same(geometry))).thenReturn(preparedGeometry);
        return preparedGeometry;
    }

    private ScannedLocation createScannedLocation(final Geometry geometry) {
        final ScannedLocation scannedLocation = mock(ScannedLocation.class);
        when(scannedLocation.getGeometry()).thenReturn(geometry);
        return scannedLocation;
    }
}
