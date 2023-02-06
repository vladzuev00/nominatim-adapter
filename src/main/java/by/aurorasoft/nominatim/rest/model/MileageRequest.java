package by.aurorasoft.nominatim.rest.model;

import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
public class MileageRequest {
    List<TrackPoint> trackPoints;
    int minDetectionSpeed;
    int maxMessageTimeout;

    @Value
    public static class TrackPoint implements LatLngAlt {
        Instant datetime;
        float latitude;
        float longitude;
        int altitude;
        int speed;
        boolean valid;
    }
}
