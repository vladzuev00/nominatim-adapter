package by.aurorasoft.distanceclassifier.service.distanceclassify.accumulator.pointlocator;

import by.aurorasoft.distanceclassifier.model.CityMap;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.geometry.GeometryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class TrackPointLocatorTest {
    private static final int GIVEN_CITY_SPEED_THRESHOLD = 60;

    @Mock
    private GeometryService mockedGeometryService;

    @Mock
    private CityMap mockedCityMap;

    private TrackPointLocator locator;

    @Before
    public void initializeLocator() {
        locator = new TrackPointLocator(mockedGeometryService, mockedCityMap, GIVEN_CITY_SPEED_THRESHOLD);
    }

    @Test
    public void pointShouldBeLocatedInCityBecauseOfCityGeometryContain() {
        final TrackPoint givenPoint = createPoint(true, true, 61);

        final boolean actual = locator.isCity(givenPoint);
        final boolean expected = true;
        assertEquals(expected, actual);

        verifyNoInteractions(givenPoint);
        verify(mockedGeometryService, times(1)).isAnyContain(anySet(), same(givenPoint));
        verify(mockedGeometryService, times(0)).isContain(any(PreparedGeometry.class), any(TrackPoint.class));
    }

    @Test
    public void pointShouldBeLocatedInCityBecauseOfCityGeometryContainDespiteOfUnknownLocation() {
        final TrackPoint givenPoint = createPoint(true, false, 61);

        final boolean actual = locator.isCity(givenPoint);
        final boolean expected = true;
        assertEquals(expected, actual);

        verifyNoInteractions(givenPoint);
        verify(mockedGeometryService, times(1)).isAnyContain(anySet(), same(givenPoint));
        verify(mockedGeometryService, times(0)).isContain(any(PreparedGeometry.class), any(TrackPoint.class));
    }

    @Test
    public void pointShouldBeLocatedInCityBecauseOfUnknownLocationAndSuitableSpeed() {
        final TrackPoint givenPoint = createPoint(false, false, 59);

        final boolean actual = locator.isCity(givenPoint);
        final boolean expected = true;
        assertEquals(expected, actual);

        verify(givenPoint, times(1)).getSpeed();
        verify(mockedGeometryService, times(1)).isAnyContain(anySet(), same(givenPoint));
        verify(mockedGeometryService, times(1)).isContain(any(PreparedGeometry.class), any(TrackPoint.class));
    }

    @Test
    public void pointShouldNotBeLocatedInCityBecauseOfNoContainingCityGeometryAndInsideScannedLocation() {
        final TrackPoint givenPoint = createPoint(false, true, 59);

        final boolean actual = locator.isCity(givenPoint);
        final boolean expected = false;
        assertEquals(expected, actual);

        verify(givenPoint, times(1)).getSpeed();
        verify(mockedGeometryService, times(1)).isAnyContain(anySet(), same(givenPoint));
        verify(mockedGeometryService, times(1)).isContain(any(PreparedGeometry.class), any(TrackPoint.class));
    }

    @Test
    public void pointShouldNotBeLocatedInCityBecauseOfUnknownLocationAndNotSuitableSpeed() {
        final TrackPoint givenPoint = createPoint(false, false, 61);

        final boolean actual = locator.isCity(givenPoint);
        final boolean expected = false;
        assertEquals(expected, actual);

        verify(givenPoint, times(1)).getSpeed();
        verify(mockedGeometryService, times(1)).isAnyContain(anySet(), same(givenPoint));
        verify(mockedGeometryService, times(0)).isContain(any(PreparedGeometry.class), any(TrackPoint.class));
    }

    private TrackPoint createPoint(final boolean insideCity, final boolean insideScannedLocation, final int speed) {
        final TrackPoint point = mock(TrackPoint.class);
        mockLocation(point, insideCity, insideScannedLocation);
        when(point.getSpeed()).thenReturn(speed);
        return point;
    }

    private void mockLocation(final TrackPoint point, final boolean insideCity, final boolean insideScannedLocation) {
        final Set<PreparedGeometry> cityGeometries = mockCityGeometries();
        when(mockedGeometryService.isAnyContain(same(cityGeometries), same(point))).thenReturn(insideCity);
        final PreparedGeometry scannedGeometry = mockScannedGeometry();
        when(mockedGeometryService.isContain(same(scannedGeometry), same(point))).thenReturn(insideScannedLocation);
    }

    @SuppressWarnings("unchecked")
    private Set<PreparedGeometry> mockCityGeometries() {
        final Set<PreparedGeometry> cityGeometries = mock(Set.class);
        when(mockedCityMap.getCityGeometries()).thenReturn(cityGeometries);
        return cityGeometries;
    }

    private PreparedGeometry mockScannedGeometry() {
        final PreparedGeometry scannedGeometry = mock(PreparedGeometry.class);
        when(mockedCityMap.getScannedGeometry()).thenReturn(scannedGeometry);
        return scannedGeometry;
    }
}
