package by.aurorasoft.distanceclassifier.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackPointTest {

    @Test
    public void latitudeShouldBeGot() {
        final double givenLatitude = 5.5555555555555555;
        final TrackPoint givenPoint = TrackPoint.builder()
                .coordinate(new Coordinate(givenLatitude, 6.6))
                .build();

        final float actual = givenPoint.getLatitude();
        final float expected = (float) givenLatitude;
        assertEquals(expected, actual, 0);
    }

    @Test
    public void longitudeShouldBeGot() {
        final double givenLongitude = 6.6666666666666666;
        final TrackPoint givenPoint = TrackPoint.builder()
                .coordinate(new Coordinate(5.5, givenLongitude))
                .build();

        final float actual = givenPoint.getLongitude();
        final float expected = (float) givenLongitude;
        assertEquals(expected, actual, 0);
    }
}
