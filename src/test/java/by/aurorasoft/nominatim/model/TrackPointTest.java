package by.aurorasoft.nominatim.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackPointTest {

    @Test
    public void latitudeShouldBeGot() {
        final float givenLatitude = 5.5F;
        final float givenLongitude = 6.6F;
        final TrackPoint givenPoint = createPoint(givenLatitude, givenLongitude);

        final float actual = givenPoint.getLatitude();
        assertEquals(givenLatitude, actual, 0.);
    }

    @Test
    public void longitudeShouldBeGot() {
        final float givenLatitude = 7.7F;
        final float givenLongitude = 8.8F;
        final TrackPoint givenPoint = createPoint(givenLatitude, givenLongitude);

        final float actual = givenPoint.getLongitude();
        assertEquals(givenLongitude, actual, 0.);
    }

    private static TrackPoint createPoint(final float latitude, final float longitude) {
        return TrackPoint.builder()
                .coordinate(new Coordinate(latitude, longitude))
                .build();
    }
}
