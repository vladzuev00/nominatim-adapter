package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.TempMileageRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.TempMileageRequest.TempTrackPointRequest;
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
    private TrackPointFactory mockedPointFactory;

    private TrackFactory trackFactory;

    @Before
    public void initializeTrackFactory() {
        trackFactory = new TrackFactory(mockedPointFactory);
    }

    @Test
    public void trackShouldBeCreated() {
        final TempTrackPointRequest firstGivenRequestPoint = mock(TempTrackPointRequest.class);
        final TempTrackPointRequest secondGivenRequestPoint = mock(TempTrackPointRequest.class);
        final List<TempTrackPointRequest> givenRequestPoints = List.of(firstGivenRequestPoint, secondGivenRequestPoint);
        final TempMileageRequest givenRequest = createRequest(givenRequestPoints);

        final TrackPoint firstGivenPoint = bindTrackPoint(firstGivenRequestPoint);
        final TrackPoint secondGivenPoint = bindTrackPoint(secondGivenRequestPoint);

        final Track actual = trackFactory.create(givenRequest);
        final Track expected = new Track(List.of(firstGivenPoint, secondGivenPoint));
        assertEquals(expected, actual);
    }

    private static TempMileageRequest createRequest(final List<TempTrackPointRequest> trackPoints) {
        return TempMileageRequest.builder()
                .trackPoints(trackPoints)
                .build();
    }

    private TrackPoint bindTrackPoint(final TempTrackPointRequest request) {
        final TrackPoint point = mock(TrackPoint.class);
        when(mockedPointFactory.create(same(request))).thenReturn(point);
        return point;
    }
}
