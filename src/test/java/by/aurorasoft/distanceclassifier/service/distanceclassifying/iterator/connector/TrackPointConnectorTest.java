package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.connector;

import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackPointConnectorTest {
    private final TrackPointConnector connector = new TrackPointConnector();

    @Test
    public void pointsShouldBeConnected() {
        final TrackPoint firstGivenPoint = new TrackPoint(
                new Coordinate(5.5, 6.6),
                60,
                new Distance(80, 130),
                new Distance(90, 150)
        );

        final Coordinate secondGivenPointCoordinate = new Coordinate(7.7, 8.8);
        final int secondGivenPointSpeed = 70;
        final double secondGivenPointGpsAbsolute = 220;
        final double secondGivenPointOdometerAbsolute = 270;
        final TrackPoint secondGivenPoint = new TrackPoint(
                secondGivenPointCoordinate,
                secondGivenPointSpeed,
                new Distance(20, secondGivenPointGpsAbsolute),
                new Distance(30, secondGivenPointOdometerAbsolute)
        );

        final TrackPoint actual = connector.connect(firstGivenPoint, secondGivenPoint);
        final TrackPoint expected = new TrackPoint(
                secondGivenPointCoordinate,
                secondGivenPointSpeed,
                new Distance(90, secondGivenPointGpsAbsolute),
                new Distance(120, secondGivenPointOdometerAbsolute)
        );
        assertEquals(expected, actual);
    }
}
