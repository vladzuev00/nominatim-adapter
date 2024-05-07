//package by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader;
//
//import by.aurorasoft.distanceclassifier.crud.model.dto.City.CityGeometry;
//import by.aurorasoft.distanceclassifier.crud.service.CityService;
//import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
//import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.preparer.GeometryPreparer;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.locationtech.jts.geom.LineString;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Set;
//import java.util.stream.Stream;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class TrackCityGeometryLoaderFromRepositoryTest {
//
//    @Mock
//    private CityService mockedCityService;
//
//    @Mock
//    private GeometryPreparer mockedGeometryPreparer;
//
//    private TrackCityMapLoaderFromRepository loader;
//
//    private boolean streamWasClosed;
//
//    @Before
//    public void initializeLoader() {
//        loader = new TrackCityMapLoaderFromRepository(null, null, mockedCityService, mockedGeometryPreparer);
//    }
//
//    @Before
//    public void resetStreamWasClosed() {
//        streamWasClosed = false;
//    }
//
//    @Test
//    public void geometriesShouldBeLoadedInternally() {
//        final LineString givenLine = mock(LineString.class);
//
//        final CityGeometry firstGivenGeometry = mock(CityGeometry.class);
//        final CityGeometry secondGivenGeometry = mock(CityGeometry.class);
//        final Stream<CityGeometry> givenGeometries = createStreamCapturingClose(firstGivenGeometry, secondGivenGeometry);
//        when(mockedCityService.findIntersectedCityGeometries(same(givenLine))).thenReturn(givenGeometries);
//
//        final PreparedCityGeometry firstGivenPreparedGeometry = mockPreparedGeometryFor(firstGivenGeometry);
//        final PreparedCityGeometry secondGivenPreparedGeometry = mockPreparedGeometryFor(secondGivenGeometry);
//
//        final Set<PreparedCityGeometry> actual = loader.loadInternal(givenLine);
//        final Set<PreparedCityGeometry> expected = Set.of(firstGivenPreparedGeometry, secondGivenPreparedGeometry);
//        assertEquals(expected, actual);
//
//        assertTrue(streamWasClosed);
//    }
//
//    private PreparedCityGeometry mockPreparedGeometryFor(final CityGeometry geometry) {
//        final PreparedCityGeometry preparedGeometry = mock(PreparedCityGeometry.class);
//        when(mockedGeometryPreparer.prepare(same(geometry))).thenReturn(preparedGeometry);
//        return preparedGeometry;
//    }
//
//    private Stream<CityGeometry> createStreamCapturingClose(final CityGeometry... geometries) {
//        return Stream.of(geometries).onClose(() -> streamWasClosed = true);
//    }
//}
