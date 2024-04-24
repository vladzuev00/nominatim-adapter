package by.aurorasoft.mileagecalculator.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackPointTest {

    @Test
    public void latitudeShouldBeGot() {
        final double givenLatitude = 5.5;
        final double givenLongitude = 6.6;
        final TrackPoint givenPoint = createPoint(givenLatitude, givenLongitude);

        final double actual = givenPoint.getLatitude();
        assertEquals(givenLatitude, actual, 0.00001);
    }

    @Test
    public void longitudeShouldBeGot() {
        final double givenLatitude = 7.7;
        final double givenLongitude = 8.8;
        final TrackPoint givenPoint = createPoint(givenLatitude, givenLongitude);

        final double actual = givenPoint.getLongitude();
        assertEquals(givenLongitude, actual, 0.00001);
    }

    private static TrackPoint createPoint(final double latitude, final double longitude) {
        return TrackPoint.builder()
                .coordinate(new Coordinate(latitude, longitude))
                .build();
    }
}
