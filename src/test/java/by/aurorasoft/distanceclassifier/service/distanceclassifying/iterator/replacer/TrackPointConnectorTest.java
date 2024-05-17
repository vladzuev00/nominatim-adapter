package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.replacer;

import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class TrackPointConnectorTest {
    private final TrackPointConnector connector = new TrackPointConnector();

    @Test
    public void pointsShouldBeConnected() {
        final TrackPoint firstGivenPoint = new TrackPoint(
                new Coordinate(5.5, 6.6),
                60,
                new Distance(40, 170),
                new Distance(50, 180)
        );

        final Coordinate secondGivenPointCoordinate = new Coordinate(7.7, 8.8);
        final int secondGivenPointSpeed = 70;
        final double secondGivenPointGpsAbsolute = 220;
        final double secondGivenPointOdometerAbsolute = 230;
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
                new Distance(100, secondGivenPointOdometerAbsolute)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void samePointsShouldBeConnected() {
        final TrackPoint givenPoint = TrackPoint.builder().build();

        final TrackPoint actual = connector.connect(givenPoint, givenPoint);
        assertSame(givenPoint, actual);
    }

    @Test
    public void pointsWithSameCoordinateShouldBeConnected() {
        final Coordinate givenCoordinate = new Coordinate(7.7, 8.8);
        final TrackPoint firstGivenPoint = createPoint(givenCoordinate);
        final TrackPoint secondGivenPoint = createPoint(givenCoordinate);

        final TrackPoint actual = connector.connect(firstGivenPoint, secondGivenPoint);
        assertSame(secondGivenPoint, actual);
    }

    private TrackPoint createPoint(final Coordinate coordinate) {
        return TrackPoint.builder()
                .coordinate(coordinate)
                .build();
    }
}
