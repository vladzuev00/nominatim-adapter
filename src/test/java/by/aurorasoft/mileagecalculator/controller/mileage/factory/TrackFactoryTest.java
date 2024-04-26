package by.aurorasoft.mileagecalculator.controller.mileage.factory;

import by.aurorasoft.mileagecalculator.controller.mileage.model.MileageRequest;
import by.aurorasoft.mileagecalculator.controller.mileage.model.MileageRequest.PointRequest;
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
        final PointRequest firstGivenPointRequest = mock(PointRequest.class);
        final PointRequest secondGivenPointRequest = mock(PointRequest.class);
        final MileageRequest givenRequest = createRequest(List.of(firstGivenPointRequest, secondGivenPointRequest));

        final TrackPoint firstGivenPoint = bindTrackPoint(firstGivenPointRequest);
        final TrackPoint secondGivenPoint = bindTrackPoint(secondGivenPointRequest);

        final Track actual = trackFactory.create(givenRequest);
        final Track expected = new Track(List.of(firstGivenPoint, secondGivenPoint));
        assertEquals(expected, actual);
    }

    private static MileageRequest createRequest(final List<PointRequest> trackPoints) {
        return MileageRequest.builder()
                .trackPoints(trackPoints)
                .build();
    }

    private TrackPoint bindTrackPoint(final PointRequest request) {
        final TrackPoint point = mock(TrackPoint.class);
        when(mockedPointFactory.create(same(request))).thenReturn(point);
        return point;
    }
}
