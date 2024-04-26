package by.aurorasoft.distanceclassifier.controller.classifydistance.factory;

import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest;
import by.aurorasoft.distanceclassifier.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
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
        final ClassifyDistanceRequest givenRequest = createRequest(List.of(firstGivenPointRequest, secondGivenPointRequest));

        final TrackPoint firstGivenPoint = bindTrackPoint(firstGivenPointRequest);
        final TrackPoint secondGivenPoint = bindTrackPoint(secondGivenPointRequest);

        final Track actual = trackFactory.create(givenRequest);
        final Track expected = new Track(List.of(firstGivenPoint, secondGivenPoint));
        assertEquals(expected, actual);
    }

    private static ClassifyDistanceRequest createRequest(final List<PointRequest> trackPoints) {
        return ClassifyDistanceRequest.builder()
                .trackPoints(trackPoints)
                .build();
    }

    private TrackPoint bindTrackPoint(final PointRequest request) {
        final TrackPoint point = mock(TrackPoint.class);
        when(mockedPointFactory.create(same(request))).thenReturn(point);
        return point;
    }
}
