package by.aurorasoft.distanceclassifier.service.distanceclassifying.iterator.replacer;

import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.nhorushko.classifieddistance.Distance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class TrackPointReplacerTest {
    private final TrackPointReplacer replacer = new TrackPointReplacer();

    @Test
    public void pointsShouldBeReplaced() {
        final TrackPoint givenExisting = new TrackPoint(
                new Coordinate(5.5, 6.6),
                60,
                new Distance(40, 170),
                new Distance(50, 180)
        );

        final Coordinate givenReplacementCoordinate = new Coordinate(7.7, 8.8);
        final int givenReplacementSpeed = 70;
        final double givenReplacementGpsAbsolute = 220;
        final double givenReplacementOdometerAbsolute = 230;
        final TrackPoint givenReplacement = new TrackPoint(
                givenReplacementCoordinate,
                givenReplacementSpeed,
                new Distance(20, givenReplacementGpsAbsolute),
                new Distance(30, givenReplacementOdometerAbsolute)
        );

        final TrackPoint actual = replacer.replace(givenExisting, givenReplacement);
        final TrackPoint expected = new TrackPoint(
                givenReplacementCoordinate,
                givenReplacementSpeed,
                new Distance(90, givenReplacementGpsAbsolute),
                new Distance(100, givenReplacementOdometerAbsolute)
        );
        assertEquals(expected, actual);
    }

    @Test
    public void samePointsShouldBeReplaced() {
        final TrackPoint givenPoint = TrackPoint.builder().build();

        final TrackPoint actual = replacer.replace(givenPoint, givenPoint);
        assertSame(givenPoint, actual);
    }

    @Test
    public void pointsWithSameCoordinateShouldBeReplaced() {
        final Coordinate givenCoordinate = new Coordinate(7.7, 8.8);
        final TrackPoint givenExisting = createPoint(givenCoordinate);
        final TrackPoint givenReplacement = createPoint(givenCoordinate);

        final TrackPoint actual = replacer.replace(givenExisting, givenReplacement);
        assertSame(givenReplacement, actual);
    }

    private TrackPoint createPoint(final Coordinate coordinate) {
        return TrackPoint.builder()
                .coordinate(coordinate)
                .build();
    }
}
