package by.aurorasoft.nominatim.controller.mileage.factory;

import by.aurorasoft.nominatim.controller.mileage.model.MileageRequest.TrackPointRequest;
import by.aurorasoft.nominatim.model.TrackPoint;
import org.junit.Test;

import java.time.Instant;

import static java.time.Instant.parse;
import static org.junit.Assert.assertEquals;

public final class TrackPointFactoryTest {
    private final TrackPointFactory factory = new TrackPointFactory();

    @Test
    public void pointShouldBeCreated() {
        final Instant givenDateTime = parse("2007-12-03T10:15:30.00Z");
        final float givenLatitude = 5.5F;
        final float givenLongitude = 6.6F;
        final int givenAltitude = 10;
        final int givenSpeed = 15;
        final boolean givenValid = true;
        final TrackPointRequest givenRequest = new TrackPointRequest(
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenAltitude,
                givenSpeed,
                givenValid
        );

        final TrackPoint actual = factory.create(givenRequest);
        final TrackPoint expected = new TrackPoint(
                givenDateTime,
                givenLatitude,
                givenLongitude,
                givenAltitude,
                givenSpeed,
                givenValid
        );
        assertEquals(expected, actual);
    }
}
