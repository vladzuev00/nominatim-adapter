//package by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator;
//
//import by.aurorasoft.distanceclassifier.model.PreparedCityGeometry;
//import by.aurorasoft.distanceclassifier.model.TrackPoint;
//import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class TrackPointLocatorTest {
//    private static final int GIVEN_CITY_SPEED_THRESHOLD = 60;
//
//    @Mock
//    private GeometryService mockedGeometryService;
//
//    @Mock
//    private Set<PreparedCityGeometry> mockedCityGeometries;
//
//    private TrackPointLocator locator;
//
//    @Before
//    public void initializeLocator() {
//        locator = new TrackPointLocator(mockedGeometryService, mockedCityGeometries, GIVEN_CITY_SPEED_THRESHOLD);
//    }
//
//    @Test
//    public void pointShouldBeLocatedInCityBecauseOfLocationIsExactCity() {
//        final TrackPoint givenPoint = mock(TrackPoint.class);
//
//        mockLocation(givenPoint, true, true);
//
//        final boolean actual = locator.isCity(givenPoint);
//        final boolean expected = true;
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenPoint);
//        verify(mockedGeometryService, times(1)).isAnyContain(same(mockedCityGeometries), same(givenPoint));
//        verify(mockedGeometryService, times(0)).isAnyBoundingBoxContain(anySet(), any(TrackPoint.class));
//    }
//
//    @Test
//    public void pointShouldBeLocatedInCityBecauseOfLocationIsUnknownAndSpeedIsCity() {
//        final TrackPoint givenPoint = createPoint(50);
//
//        mockLocation(givenPoint, false, false);
//
//        final boolean actual = locator.isCity(givenPoint);
//        final boolean expected = true;
//        assertEquals(expected, actual);
//
//        verify(givenPoint, times(1)).getSpeed();
//        verify(mockedGeometryService, times(1)).isAnyContain(same(mockedCityGeometries), same(givenPoint));
//        verify(mockedGeometryService, times(1)).isAnyBoundingBoxContain(same(mockedCityGeometries), same(givenPoint));
//    }
//
//    @Test
//    public void pointShouldNotBeLocatedInCityBecauseOfLocationIsBoundingBox() {
//        final TrackPoint givenPoint = createPoint(50);
//
//        mockLocation(givenPoint, false, true);
//
//        final boolean actual = locator.isCity(givenPoint);
//        final boolean expected = false;
//        assertEquals(expected, actual);
//
//        verify(givenPoint, times(1)).getSpeed();
//        verify(mockedGeometryService, times(1)).isAnyContain(same(mockedCityGeometries), same(givenPoint));
//        verify(mockedGeometryService, times(1)).isAnyBoundingBoxContain(same(mockedCityGeometries), same(givenPoint));
//    }
//
//    @Test
//    public void pointShouldNotBeLocatedInCityBecauseOfLocationIsUnknownAndSpeedIsNotCity() {
//        final TrackPoint givenPoint = createPoint(61);
//
//        mockLocation(givenPoint, false, false);
//
//        final boolean actual = locator.isCity(givenPoint);
//        final boolean expected = false;
//        assertEquals(expected, actual);
//
//        verify(givenPoint, times(1)).getSpeed();
//        verify(mockedGeometryService, times(1)).isAnyContain(same(mockedCityGeometries), same(givenPoint));
//        verify(mockedGeometryService, times(0)).isAnyBoundingBoxContain(anySet(), any(TrackPoint.class));
//    }
//
//    private void mockLocation(final TrackPoint point, final boolean insideCity, final boolean insideBoundingBox) {
//        when(mockedGeometryService.isAnyContain(same(mockedCityGeometries), same(point))).thenReturn(insideCity);
//        when(mockedGeometryService.isAnyBoundingBoxContain(same(mockedCityGeometries), same(point))).thenReturn(insideBoundingBox);
//    }
//
//    private static TrackPoint createPoint(final int speed) {
//        final TrackPoint point = mock(TrackPoint.class);
//        when(point.getSpeed()).thenReturn(speed);
//        return point;
//    }
//}
