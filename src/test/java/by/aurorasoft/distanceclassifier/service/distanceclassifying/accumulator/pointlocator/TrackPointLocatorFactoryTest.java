//package by.aurorasoft.distanceclassifier.service.distanceclassifying.pointlocator;
//
//import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
//import by.aurorasoft.distanceclassifier.model.Track;
//import by.aurorasoft.distanceclassifier.service.distanceclassifying.maploader.TrackCityMapLoader;
//import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Set;
//
//import static by.aurorasoft.distanceclassifier.testutil.ReflectionUtil.getFieldValue;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class TrackPointLocatorFactoryTest {
//    private static final String FIELD_NAME_GEOMETRY_SERVICE = "geometryService";
//    private static final String FIELD_NAME_CITY_GEOMETRIES = "cityGeometries";
//    private static final String FIELD_NAME_CITY_SPEED_THRESHOLD = "citySpeedThreshold";
//
//    @Mock
//    private TrackCityMapLoader mockedTrackCityGeometryLoader;
//
//    @Mock
//    private GeometryService mockedGeometryService;
//
//    private TrackPointLocatorFactory factory;
//
//    @Before
//    public void initializeFactory() {
//        factory = new TrackPointLocatorFactory(mockedTrackCityGeometryLoader, mockedGeometryService);
//    }
//
//    @Test
//    @SuppressWarnings("unchecked")
//    public void locatorShouldBeCreated() {
//        final Track givenTrack = mock(Track.class);
//        final int givenCitySpeedThreshold = 60;
//
//        final Set<PreparedCityGeometry> givenCityGeometries = mock(Set.class);
//        when(mockedTrackCityGeometryLoader.load(same(givenTrack))).thenReturn(givenCityGeometries);
//
//        final TrackPointLocator actual = factory.create(givenTrack, givenCitySpeedThreshold);
//
//        final GeometryService actualGeometryService = getGeometryService(actual);
//        assertSame(mockedGeometryService, actualGeometryService);
//
//        final Set<PreparedCityGeometry> actualCityGeometries = getCityGeometries(actual);
//        assertSame(givenCityGeometries, actualCityGeometries);
//
//        final int actualCitySpeedThreshold = getCitySpeedThreshold(actual);
//        assertEquals(givenCitySpeedThreshold, actualCitySpeedThreshold);
//    }
//
//    private GeometryService getGeometryService(final TrackPointLocator locator) {
//        return getFieldValue(locator, FIELD_NAME_GEOMETRY_SERVICE, GeometryService.class);
//    }
//
//    @SuppressWarnings("unchecked")
//    private Set<PreparedCityGeometry> getCityGeometries(final TrackPointLocator locator) {
//        return getFieldValue(locator, FIELD_NAME_CITY_GEOMETRIES, Set.class);
//    }
//
//    private int getCitySpeedThreshold(final TrackPointLocator locator) {
//        return getFieldValue(locator, FIELD_NAME_CITY_SPEED_THRESHOLD, Integer.class);
//    }
//}
