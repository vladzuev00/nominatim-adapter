package by.aurorasoft.nominatim.model;

import by.nhorushko.distancecalculator.LatLngAlt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@AllArgsConstructor
@Builder
public class TrackPoint implements LatLngAlt {
    Instant datetime;
    Coordinate coordinate;
    int altitude;
    int speed;
    boolean valid;

    @Override
    public float getLatitude() {
        return (float) coordinate.getLatitude();
    }

    @Override
    public float getLongitude() {
        return (float) coordinate.getLongitude();
    }
}
