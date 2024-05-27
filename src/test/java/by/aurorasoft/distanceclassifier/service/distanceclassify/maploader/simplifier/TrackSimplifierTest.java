package by.aurorasoft.distanceclassifier.service.distanceclassify.maploader.simplifier;

import by.aurorasoft.distanceclassifier.config.property.ClassifyDistanceProperty;
import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.Track;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.trackfilter.TrackFilterI;
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

    @Mock
    private TrackFilterI mockedTrackFilter;

    @Mock
    private ClassifyDistanceProperty mockedProperty;

    private TrackSimplifier simplifier;

    @Before
    public void initializeSimplifier() {
        simplifier = new TrackSimplifier(mockedTrackFilter, mockedProperty);
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

        final double givenEpsilon = 0.00015;
        when(mockedProperty.getTrackSimplifyEpsilon()).thenReturn(givenEpsilon);

        final List givenFilteredPoints = List.of(createPoint(4.4, 5.5), createPoint(8.8, 9.9));
        when(mockedTrackFilter.filter(same(givenPoints), eq(givenEpsilon))).thenReturn(givenFilteredPoints);

        final Track actual = simplifier.simplify(givenTrack);
        final Track expected = new Track(givenFilteredPoints);
        assertEquals(expected, actual);
    }

    private TrackPoint createPoint(final double latitude, final double longitude) {
        return TrackPoint.builder()
                .coordinate(new Coordinate(latitude, longitude))
                .build();
    }
}
