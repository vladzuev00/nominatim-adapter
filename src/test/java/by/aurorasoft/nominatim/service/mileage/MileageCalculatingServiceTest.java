package by.aurorasoft.nominatim.service.mileage;

import by.aurorasoft.nominatim.model.Mileage;
import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
import by.aurorasoft.nominatim.service.geometry.GeometryService;
import by.aurorasoft.nominatim.service.mileage.loader.TrackCityGeometryLoader;
import by.nhorushko.distancecalculator.DistanceCalculator;
import by.nhorushko.distancecalculator.DistanceCalculatorSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class MileageCalculatingServiceTest {

    @Mock
    private TrackCityGeometryLoader mockedTrackCityGeometryLoader;

    @Mock
    private GeometryService mockedGeometryService;

    @Mock
    private DistanceCalculator mockedDistanceCalculator;

    private MileageCalculatingService service;

    @Before
    public void initializeService() {
        service = new MileageCalculatingService(
                mockedTrackCityGeometryLoader,
                mockedGeometryService,
                mockedDistanceCalculator
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void mileageShouldBeCalculated() {
        final TrackPoint firstGivenPoint = mock(TrackPoint.class);
        final TrackPoint secondGivenPoint = mock(TrackPoint.class);
        final TrackPoint thirdGivenPoint = mock(TrackPoint.class);
        final TrackPoint fourthGivenPoint = mock(TrackPoint.class);
        final Track givenTrack = new Track(List.of(firstGivenPoint, secondGivenPoint, thirdGivenPoint, fourthGivenPoint));

        final DistanceCalculatorSettings givenSettings = mock(DistanceCalculatorSettings.class);

        final List<PreparedGeometry> givenCityGeometries = mock(List.class);
        when(mockedTrackCityGeometryLoader.load(same(givenTrack))).thenReturn(givenCityGeometries);

        setBelonging(secondGivenPoint, givenCityGeometries, true);
        setBelonging(thirdGivenPoint, givenCityGeometries, false);
        setBelonging(fourthGivenPoint, givenCityGeometries, true);

        setDistanceBetweenPoint(firstGivenPoint, secondGivenPoint, givenSettings, 1.1);
        setDistanceBetweenPoint(secondGivenPoint, thirdGivenPoint, givenSettings, 2.2);
        setDistanceBetweenPoint(thirdGivenPoint, fourthGivenPoint, givenSettings, 3.3);

        final Mileage actual = service.calculate(givenTrack, givenSettings);
        final Mileage expected = new Mileage(4.4, 2.2);
        assertEquals(expected, actual);

        verify(mockedGeometryService, times(0)).isAnyContain(same(givenCityGeometries), same(firstGivenPoint));
    }

    @Test
    public void mileageShouldBeCalculatedByEmptyTrack() {
        final Track givenTrack = new Track(emptyList());
        final DistanceCalculatorSettings givenSettings = mock(DistanceCalculatorSettings.class);

        final Mileage actual = service.calculate(givenTrack, givenSettings);
        final Mileage expected = new Mileage(0, 0);
        assertEquals(expected, actual);

        verifyNoInteractions(mockedTrackCityGeometryLoader);
        verifyNoInteractions(mockedGeometryService);
        verifyNoInteractions(mockedDistanceCalculator);
    }

    private void setDistanceBetweenPoint(final TrackPoint first,
                                         final TrackPoint second,
                                         final DistanceCalculatorSettings settings,
                                         final double distance) {
        when(mockedDistanceCalculator.calculateDistance(same(first), same(second), same(settings))).thenReturn(distance);
    }

    private void setBelonging(final TrackPoint point, final List<PreparedGeometry> geometries, final boolean belonged) {
        when(mockedGeometryService.isAnyContain(same(geometries), same(point))).thenReturn(belonged);
    }
}
