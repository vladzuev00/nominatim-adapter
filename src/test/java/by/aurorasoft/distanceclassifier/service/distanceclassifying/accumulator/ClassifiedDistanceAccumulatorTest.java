package by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator;

import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassifying.accumulator.pointlocator.TrackPointLocator;
import by.nhorushko.classifieddistance.ClassifiedDistance;
import by.nhorushko.classifieddistance.ClassifiedDistanceStorage;
import by.nhorushko.classifieddistance.Distance;
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
public final class ClassifiedDistanceAccumulatorTest {

    @Mock
    private TrackPointLocator mockedPointLocator;

    @Test
    public void distanceShouldBeAccumulated() {
        final ClassifiedDistanceAccumulator givenAccumulator = createAccumulator();
        final List<TrackPoint> givenPoints = List.of(
                createPoint(1.1, 2.2, true),
                createPoint(3.3, 4.4, false),
                createPoint(5.5, 6.6, true),
                createPoint(7.7, 8.8, false),
                createPoint(9.9, 10.1, true),
                createPoint(11.1, 12.2, false),
                createPoint(13.3, 14.4, true)
        );

        givenPoints.forEach(givenAccumulator::accumulate);

        final ClassifiedDistanceStorage actual = givenAccumulator.get();
        final ClassifiedDistanceStorage expected = new ClassifiedDistanceStorage(
                new ClassifiedDistance(29.8, 22.1),
                new ClassifiedDistance(33.3, 25.4)
        );
        assertEquals(expected, actual);
    }

    private ClassifiedDistanceAccumulator createAccumulator() {
        return new ClassifiedDistanceAccumulator(mockedPointLocator);
    }

    private TrackPoint createPoint(final double gpsDelta, final double odometerDelta, final boolean locatedInCity) {
        final TrackPoint point = mock(TrackPoint.class);
        final Distance gpsDistance = createDistance(gpsDelta);
        final Distance odometerDistance = createDistance(odometerDelta);
        when(point.getGpsDistance()).thenReturn(gpsDistance);
        when(point.getOdometerDistance()).thenReturn(odometerDistance);
        when(mockedPointLocator.isCity(same(point))).thenReturn(locatedInCity);
        return point;
    }

    private Distance createDistance(final double relative) {
        final Distance distance = mock(Distance.class);
        when(distance.getRelative()).thenReturn(relative);
        return distance;
    }
}
