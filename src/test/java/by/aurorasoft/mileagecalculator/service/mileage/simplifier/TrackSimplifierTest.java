package by.aurorasoft.mileagecalculator.service.mileage.simplifier;

import by.aurorasoft.mileagecalculator.model.Coordinate;
import by.aurorasoft.mileagecalculator.model.Track;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import by.nhorushko.trackfilter.TrackFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

//TODO
@RunWith(MockitoJUnitRunner.class)
public final class TrackSimplifierTest {
    private static final double GIVEN_EPSILON = 0.0015;

    @Mock
    private TrackFilter mockedTrackFilter;

    private TrackSimplifier simplifier;

    @Before
    public void initializeSimplifier() {
        simplifier = new TrackSimplifier(mockedTrackFilter, GIVEN_EPSILON);
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void trackShouldBeSimplified() {
        final List<TrackPoint> givenPoints = List.of(
                createPoint(4.4, 5.5),
                createPoint(6.6, 7.7),
                createPoint(8.8, 9.9)
        );
        final Track givenTrack = new Track(givenPoints);

        final List givenFilteredPoints = List.of(
                createPoint(4.4, 5.5),
                createPoint(8.8, 9.9)
        );
//        when(mockedTrackFilter.filter(same(givenPoints), eq(GIVEN_EPSILON))).thenReturn(givenFilteredPoints);

        final Track actual = simplifier.simplify(givenTrack);
        final Track expected = new Track(givenFilteredPoints);
        assertEquals(expected, actual);
    }

    private static TrackPoint createPoint(final double latitude, final double longitude) {
        return null;
//        return TrackPoint.builder()
//                .coordinate(new Coordinate(latitude, longitude))
//                .build();
    }
}
