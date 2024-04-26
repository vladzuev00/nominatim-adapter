package by.aurorasoft.mileagecalculator.controller.classifydistance.factory;

import by.aurorasoft.mileagecalculator.controller.classifydistance.model.ClassifyDistanceRequest.DistanceRequest;
import by.aurorasoft.mileagecalculator.controller.classifydistance.model.ClassifyDistanceRequest.PointRequest;
import by.aurorasoft.mileagecalculator.model.Coordinate;
import by.aurorasoft.mileagecalculator.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackPointFactoryTest {
    private final TrackPointFactory factory = new TrackPointFactory();

    @Test
    public void pointShouldBeCreated() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;
        final int givenSpeed = 50;
        final double givenGpsRelative = 11.1;
        final double givenGpsAbsolute = 22.2;
        final double givenOdometerRelative = 33.3;
        final double givenOdometerAbsolute = 44.4;
        final PointRequest givenRequest = new PointRequest(
                givenLatitude,
                givenLongitude,
                givenSpeed,
                new DistanceRequest(givenGpsRelative, givenGpsAbsolute),
                new DistanceRequest(givenOdometerRelative, givenOdometerAbsolute)
        );

        final TrackPoint actual = factory.create(givenRequest);
        final TrackPoint expected = new TrackPoint(
                new Coordinate(givenLatitude, givenLongitude),
                givenSpeed,
                new Distance(givenGpsRelative, givenGpsAbsolute),
                new Distance(givenOdometerRelative, givenOdometerAbsolute)
        );
        assertEquals(expected, actual);
    }
}
