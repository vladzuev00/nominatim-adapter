package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.TEMPMileageRequest.TEMPTrackPointRequest;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackFactoryTest {

    @Mock
    private TEMPTrackPointFactory mockedPointFactory;

    private TrackFactory trackFactory;

    @Before
    public void initializeTrackFactory() {
        trackFactory = new TrackFactory(mockedPointFactory);
    }

    @Test
    public void trackShouldBeCreated() {
        final TEMPTrackPointRequest firstGivenRequestPoint = mock(TEMPTrackPointRequest.class);
        final TEMPTrackPointRequest secondGivenRequestPoint = mock(TEMPTrackPointRequest.class);
        final List<TEMPTrackPointRequest> givenRequestPoints = List.of(firstGivenRequestPoint, secondGivenRequestPoint);
        final TEMPMileageRequest givenRequest = createRequest(givenRequestPoints);

        final TrackPoint firstGivenPoint = bindTrackPoint(firstGivenRequestPoint);
        final TrackPoint secondGivenPoint = bindTrackPoint(secondGivenRequestPoint);

        final Track actual = trackFactory.create(givenRequest);
        final Track expected = new Track(List.of(firstGivenPoint, secondGivenPoint));
        assertEquals(expected, actual);
    }

    private static TEMPMileageRequest createRequest(final List<TEMPTrackPointRequest> trackPoints) {
        return TEMPMileageRequest.builder()
                .trackPoints(trackPoints)
                .build();
    }

    private TrackPoint bindTrackPoint(final TEMPTrackPointRequest request) {
        final TrackPoint point = mock(TrackPoint.class);
        when(mockedPointFactory.create(same(request))).thenReturn(point);
        return point;
    }
}
