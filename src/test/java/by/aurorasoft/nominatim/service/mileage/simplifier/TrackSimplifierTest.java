package by.aurorasoft.nominatim.service.mileage.simplifier;

import by.aurorasoft.nominatim.model.Track;
import by.aurorasoft.nominatim.model.TrackPoint;
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
                createPoint(4.4F, 5.5F),
                createPoint(6.6F, 7.7F),
                createPoint(8.8F, 9.9F)
        );
        final Track givenTrack = new Track(givenPoints);

        final List givenFilteredPoints = List.of(
                createPoint(4.4F, 5.5F),
                createPoint(8.8F, 9.9F)
        );
        when(mockedTrackFilter.filter(same(givenPoints), eq(GIVEN_EPSILON))).thenReturn(givenFilteredPoints);

        final Track actual = simplifier.simplify(givenTrack);
        final Track expected = new Track(givenFilteredPoints);
        assertEquals(expected, actual);
    }

    private static TrackPoint createPoint(final float latitude, final float longitude) {
        return TrackPoint.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
