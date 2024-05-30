package by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer;

import by.aurorasoft.distanceclassifier.model.Coordinate;
import by.aurorasoft.distanceclassifier.model.TrackPoint;
import by.aurorasoft.distanceclassifier.service.distanceclassify.iterator.pointreplacer.exception.TrackPointWrongOrderException;
import by.nhorushko.classifieddistance.Distance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackPointReplacerTest {
    private final TrackPointReplacer replacer = new TrackPointReplacer();

    @Test
    public void pointShouldBeReplaced() {
        final TrackPoint givenExisting = new TrackPoint(
                new Coordinate(5.5, 6.6),
                60,
                new Distance(40, 170),
                new Distance(50, 200)
        );

        final Coordinate replacementGivenCoordinate = new Coordinate(7.7, 8.8);
        final int replacementGivenSpeed = 70;
        final double replacementGivenGpsAbsolute = 220;
        final double replacementGivenOdometerAbsolute = 270;
        final TrackPoint givenReplacement = new TrackPoint(
                replacementGivenCoordinate,
                replacementGivenSpeed,
                new Distance(20, replacementGivenGpsAbsolute),
                new Distance(30, replacementGivenOdometerAbsolute)
        );

        final TrackPoint actual = replacer.replace(givenExisting, givenReplacement);
        final TrackPoint expected = new TrackPoint(
                replacementGivenCoordinate,
                replacementGivenSpeed,
                new Distance(90, replacementGivenGpsAbsolute),
                new Distance(120, replacementGivenOdometerAbsolute)
        );
        assertEquals(expected, actual);
    }

    @Test(expected = TrackPointWrongOrderException.class)
    public void pointShouldNotBeReplacedBecauseOfWrongOrderByGpsAbsolute() {
        final TrackPoint givenExisting = new TrackPoint(
                new Coordinate(5.5, 6.6),
                60,
                new Distance(40, 170),
                new Distance(50, 200)
        );
        final TrackPoint givenReplacement = new TrackPoint(
                new Coordinate(7.7, 7.7),
                70,
                new Distance(50, 169),
                new Distance(60, 260)
        );

        replacer.replace(givenExisting, givenReplacement);
    }

    @Test(expected = TrackPointWrongOrderException.class)
    public void pointShouldNotBeReplacedBecauseOfWrongOrderByOdometerAbsolute() {
        final TrackPoint givenExisting = new TrackPoint(
                new Coordinate(5.5, 6.6),
                60,
                new Distance(40, 170),
                new Distance(50, 200)
        );
        final TrackPoint givenReplacement = new TrackPoint(
                new Coordinate(7.7, 7.7),
                70,
                new Distance(50, 210),
                new Distance(60, 199)
        );

        replacer.replace(givenExisting, givenReplacement);
    }
}
